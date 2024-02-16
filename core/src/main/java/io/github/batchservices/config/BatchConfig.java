package io.github.batchservices.config;

import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.listeners.StepNotificationListener;
import io.github.batchservices.processor.TransactionsItemProcessor;
import io.github.batchservices.service.FileLogService;
import io.github.batchservices.tasklets.PostprocessingTasklet;
import io.github.batchservices.tasklets.PreprocessingTasklet;
import io.github.batchservices.util.FileTypeDecider;
import io.github.batchservices.writer.CustomFormatHeaderWriter;
import io.github.batchservices.writer.TransactionsItemWriter;

import io.github.batchservices.util.BatchConstants;
import io.github.batchservices.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.ncr.di.bch.nachafiletransformer.*"})
public class BatchConfig {

	@Autowired
	StepNotificationListener stepNotificationListener;

	@Autowired
	ExecutionContextAccessor executionContextAccessor;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	@StepScope
    FileTypeDecider fileTypeDecider;

	@Autowired
	FileLogService fileLogService;

	@Autowired
	Step transformNachaTransactionsFileStep;

	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	
	@Bean
	Job batchJob() {		
		FlowJobBuilder job = jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(preProcessingStep())
				.on(Constants.FAILED).end()
				.from(preProcessingStep())
				.on(Constants.STOPPED).end()
				.from(preProcessingStep())
				.on(Constants.COMPLETED)
				.to(fileTypeDecider)
				.from(fileTypeDecider)
					.on(Constants.NACHA_TRANSACTIONS_FILE)
					.to(transformNachaTransactionsFileStep)
					.on(Constants.COMPLETED)
					.to(postProcessingStep())	
				.build();
		return  job.build();
	}

	/**************************************************************
	 * Pre/Post Processing beans initialization - BEGIN *
	 **************************************************************/

	@Bean
	Step preProcessingStep() {
		return stepBuilderFactory.get("preProcessingStep")
				.listener(stepNotificationListener)
				.listener(executionContextAccessor)
				.tasklet(preprocessingTasklet(null))
				.build();
	}

	@Bean
	Step postProcessingStep() {
		return stepBuilderFactory.get("postProcessingStep")
				.listener(executionContextAccessor)
				.tasklet(postProcessingTasklet())
				.build();
	}

	@Bean
	@StepScope
	Tasklet preprocessingTasklet(@Value("#{jobParameters[file_path]}") String filePath) {
		return new PreprocessingTasklet(filePath);
	}

	@Bean
	@StepScope
	Tasklet postProcessingTasklet() {
		return new PostprocessingTasklet();
	}

	/**************************************************************
	 * Pre/Post Processing beans initialization - END *
	 **************************************************************/

	/**************************************************************
	 * Item Writer Beans - Begin *
	 **************************************************************/

	@Bean
	@StepScope
	public TransactionsItemWriter transactionsItemWriter(){
		return new TransactionsItemWriter();
	}
	/**************************************************************
	 * Item Writer Beans - End *
	 **************************************************************/

	/**************************************************************
	 * Item Processor Beans - Start *
	 **************************************************************/
	@Bean
	@StepScope
	public TransactionsItemProcessor transactionsItemProcessor(){
		TransactionsItemProcessor transactionsItemProcessor = new TransactionsItemProcessor();
		// transactionsItemProcessor.setBatchCounterTrackerDelegate(transactionsBatchCountTracker);
		return transactionsItemProcessor;
	}
	
    @Bean
    @StepScope
    CustomFormatHeaderWriter customFormatHeaderWriter() {
    	return new CustomFormatHeaderWriter();
    }
	
	/**************************************************************
	 * Item Processor Beans - End *
	 **************************************************************/
	

	/**************************************************************
	 * Common Config Beans - Begin *
	 **************************************************************/
	@Bean
	@StepScope
	public ExecutionContextAccessor executionContextAccessor(){
		return new ExecutionContextAccessor();
	}
	
	/**************************************************************
	 * Common Config Beans - End *
	 **************************************************************/


	/**************************************************************
	 * Header/Footer tokenizer - Begin *
	 **************************************************************/

	@Bean
	public LineTokenizer fileHeaderLineTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(BatchConstants.FILE_HEADER_FIELDS_RANGE);
		tokenizer.setNames(BatchConstants.FILE_HEADER_FIELDS);
		tokenizer.setStrict(false);
		return tokenizer;
	}

	@Bean
	public LineTokenizer fileFooterLineTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(BatchConstants.FILE_FOOTER_FIELDS_RANGE);
		tokenizer.setNames(BatchConstants.FILE_FOOTER_FIELDS);
		tokenizer.setStrict(false);
		return tokenizer;
	}

	@Bean
	public LineTokenizer batchHeaderLineTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(BatchConstants.BATCH_HEADER_FIELDS_RANGE);
		tokenizer.setNames(BatchConstants.BATCH_HEADER_FIELDS);
		tokenizer.setStrict(false);
		return tokenizer;
	}

	@Bean
	public LineTokenizer batchFooterLineTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(BatchConstants.BATCH_FOOTER_FIELDS_RANGE);
		tokenizer.setNames(BatchConstants.BATCH_FOOTER_FIELDS);
		tokenizer.setStrict(false);
		return tokenizer;
	}
	
	@Bean
	public LineTokenizer transactionsAddendaLineTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(BatchConstants.TRANSACTIONS_ADDENDA_RANGE);
		tokenizer.setNames(BatchConstants.TRANSACTIONS_ADDENDA_FIELDS);
		tokenizer.setStrict(false);
		return tokenizer;
	}
	
	/**************************************************************
	 * Header/Footer tokenizer - End *
	 **************************************************************/
}
