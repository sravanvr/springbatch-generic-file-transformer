package io.github.batchservices.config;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.batchservices.util.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.LastModifiedFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.zip.splitter.UnZipResultSplitter;
import org.springframework.integration.zip.transformer.UnZipTransformer;
import org.springframework.integration.zip.transformer.ZipResultType;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import io.github.batchservices.filepoller.CustomDirectoryScanner;
import io.github.batchservices.filepoller.FileMessageToJobRequest;

@Configuration
public class IntegrationConfig {

	private static final Logger logger = LoggerFactory.getLogger(IntegrationConfig.class);

	@Autowired
	public File inboundReadDirectory;
	
	@Autowired
	public File inboundProcessedDirectory;

	@Autowired
	public File inboundWorkDirectory;

	@Autowired
	FileUtility fileUtility;
	
	private static final String LOGGING = "LOGGING";


	/***
	 * Common Spring Integration beans used by multiple flows in this config.
	 */
	@ServiceActivator(inputChannel = LOGGING)
	@Bean
	public LoggingHandler integFlowCommonLoggingHandler() {
		LoggingHandler loggingHandler = new LoggingHandler(LoggingHandler.Level.INFO);
		return loggingHandler;
	}

	@Bean
    public TaskExecutor threadPoolTaskExecutor(@Value("${inbound.file.poller.thread.pool.size}") int poolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }
	
	@Bean
	@ServiceActivator(inputChannel = "jobChannel", outputChannel = "nullChannel")
	protected JobLaunchingMessageHandler jobLaunchingMessageHandler(JobLauncher jobLauncher) {
		return new JobLaunchingMessageHandler(jobLauncher);
	}

	/*********************************************************************************************
	 * File Poller Configuration - START 
	 *********************************************************************************************/ 
	@Bean
	public IntegrationFlow inboundFileReadingFlow(Job batchJob, JobLauncher jobLauncher, @Value("${inbound.file.poller.fixed.delay}") long period,
			@Value("${inbound.file.poller.max.messages.per.poll}") int maxMessagesPerPoll, TaskExecutor threadPoolTaskExecutor,
			MessageSource<File> fileReadingMessageSource) {

		return IntegrationFlows
				.from(fileReadingMessageSource,
						c -> c.poller(Pollers.fixedDelay(period)
								.taskExecutor(threadPoolTaskExecutor)
								.maxMessagesPerPoll(maxMessagesPerPoll)))
								.wireTap(f -> f.handle(t -> logger.debug("[INTEG_FLOW] - \"fileReadingMessageSource\" completed reading message.")))
								.wireTap(LOGGING)
								.handle(new GenericHandler<File>() {
									@Override
									@Transactional
									public Object handle(File readDirectoryFile, Map<String, Object> headers) {
										MDC.clear(); // Remove previously polled file related info to avoid inappropriate logging. 
										MDC.put("filePath", readDirectoryFile.getAbsolutePath()); // Keep this for interim logging until process-dir file is generated.
										String processDirectoryPath = inboundProcessedDirectory.getAbsolutePath() +
												File.separator + readDirectoryFile.getParentFile().getName();
										File processDirectory = new File(processDirectoryPath);																				
										if (! processDirectory.exists()) {
											processDirectory.mkdirs();
										}
										File processDirectoryFile = new File(processDirectory.getAbsolutePath(), readDirectoryFile.getName());
										// Put processDirectoryFile's file_path in MDC. Set fileLogId to blank.
										MDC.put("filePath", processDirectoryFile.getAbsolutePath());
										MDC.put("fileLogId", "");										
										return (fileUtility.moveFileToProcessDirectoryUsingLock(readDirectoryFile, processDirectoryFile));										
									}
								})
								.filter(file -> file !=null)
								.transform(fileMessageToJobRequest(batchJob))
								.wireTap(f -> f.handle(t -> logger.debug("[INTEG_FLOW] - The transformer method from FileMessageToJobRequest has been called which returns \"JobLaunchRequest\" object.")))
								.wireTap(LOGGING)
								.handle(jobLaunchingMessageHandler(jobLauncher))
								.handle(jobExecution -> {
									// This handler method gets called after the completion of the launched batch job. 
									logger.debug("[INTEG_FLOW] - Job should have been completed with payload - " + jobExecution.getPayload());
								})
								.get();

	}
	
	@Bean
	public FileReadingMessageSource fileReadingMessageSource(CustomDirectoryScanner customDirectoryScanner, 
			@Value("${inbound.filename.regex}") String regex,
			@Value("${inbound.file.poller.lastmodifiedfilter.age:20}") int lastModifiedFilterAge) {

		LastModifiedFileListFilter lastModifiedFileListFilter = new LastModifiedFileListFilter();	
				
		CompositeFileListFilter<File> filters = new CompositeFileListFilter<>();
		filters.addFilter(new RegexPatternFileListFilter(regex));
		
		lastModifiedFileListFilter.setAge(lastModifiedFilterAge, TimeUnit.SECONDS);
		filters.addFilter(lastModifiedFileListFilter);

		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setDirectory(this.inboundReadDirectory);
		source.setAutoCreateDirectory(false);
		source.setScanner(customDirectoryScanner);
		// Spring Integration 4.2: Need to setFilter and setLocker on custom scanner instead of on inbound adapter.
		customDirectoryScanner.setFilter(filters);
		return source;
	}

	@Bean
	FileMessageToJobRequest fileMessageToJobRequest(Job batchJob) {
		FileMessageToJobRequest transformer = new FileMessageToJobRequest();
		transformer.setJob(batchJob);
		transformer.setFileParameterName("file_path");
		return transformer;
	}

	/*********************************************************************************************
	 * File Poller Configuration - END 
	 *********************************************************************************************/ 
	
	/*********************************************************************************************
	 * Unzip File Poller Flow Configuration - START 
	 *********************************************************************************************/ 
	@Bean
	public IntegrationFlow unzipFilesFlow(@Value("${inbound.file.poller.fixed.delay}") long period,
			@Value("${inbound.file.poller.max.messages.per.poll}") int maxMessagesPerPoll, TaskExecutor threadPoolTaskExecutor,
			MessageSource<File> zipFilesMessageSource) {

		return IntegrationFlows
				.from(zipFilesMessageSource,
						c -> c.poller(Pollers.fixedDelay(period)
								.taskExecutor(threadPoolTaskExecutor)
								.maxMessagesPerPoll(maxMessagesPerPoll)))
								.enrichHeaders(s -> s.headerExpressions(h -> h
										.put("OriginalFilePath", "payload.parent")))
										.handle(new GenericHandler<File>() {
											@Override
											@Transactional
											public Object handle(File readDirectoryFile, Map<String, Object> headers) {
												String processDirectoryPath = inboundProcessedDirectory.getAbsolutePath() +
														File.separator + readDirectoryFile.getParentFile().getName();
												File processDirectory = new File(processDirectoryPath);

												if (!processDirectory.exists()) {
													processDirectory.mkdirs();
												}
												File processDirectoryFile = new File(processDirectory.getAbsolutePath(), readDirectoryFile.getName());
												// Put processDirectoryFile file_path in MDC. Set fileLogId to blank.
												MDC.clear();
												MDC.put("filePath", processDirectoryFile.getAbsolutePath());
												return (fileUtility.moveFileToProcessDirectory(readDirectoryFile, processDirectoryFile));																								
											}
										})
										.transform(unzipTransformer())
										.split(unZipResultSplitter())
										.handle(Files.outboundAdapter(m -> m.getHeaders().get("OriginalFilePath"))
												.deleteSourceFiles(true)
												.fileExistsMode(FileExistsMode.REPLACE))
												.get();

	}
	
	@Bean
	public FileReadingMessageSource zipFilesMessageSource(@Value("${inbound.zipfilename.regex}") String regex,
			@Value("${inbound.file.poller.lastmodifiedfilter.age:20}") int lastModifiedFilterAge)  {
		
		LastModifiedFileListFilter lastModifiedFileListFilter = new LastModifiedFileListFilter();
		
		CompositeFileListFilter<File> filters = new CompositeFileListFilter<>();
		filters.addFilter(new RegexPatternFileListFilter(regex));
		lastModifiedFileListFilter.setAge(lastModifiedFilterAge, TimeUnit.SECONDS);
		filters.addFilter(lastModifiedFileListFilter);

		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setDirectory(this.inboundReadDirectory);
		source.setAutoCreateDirectory(false);
		source.setFilter(filters);

		source.setUseWatchService(true);
		source.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE, FileReadingMessageSource.WatchEventType.MODIFY);

		return source;
	}

	public UnZipTransformer unzipTransformer() {
		UnZipTransformer unzipTransformer = new UnZipTransformer();
		unzipTransformer.setExpectSingleResult(false);
		unzipTransformer.setZipResultType(ZipResultType.FILE);
		unzipTransformer.setWorkDirectory(inboundWorkDirectory);
		unzipTransformer.setDeleteFiles(false);
		return unzipTransformer;
	}

	public UnZipResultSplitter unZipResultSplitter() {
		return new UnZipResultSplitter();
	}

	/*********************************************************************************************
	 * Unzip File Poller Flow Configuration - END 
	 *********************************************************************************************/
	
	@Bean
	public RequestHandlerRetryAdvice retryAdvice() {
		RequestHandlerRetryAdvice retryAdvice = new RequestHandlerRetryAdvice();
		retryAdvice.setRetryTemplate(retryTemplate());
		return retryAdvice;
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);
		retryTemplate.setRetryPolicy(retryPolicy);

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(2000);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		return retryTemplate;
	}

}
