package io.github.batchservices.tasklets;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.batchservices.service.FileLogService;

/***
 * 
 * @author rv250129
 *
 */

// Update File_Log table with job completion status
public class PostprocessingTasklet implements Tasklet, StepExecutionListener {
    
	private static final XLogger logger = XLoggerFactory
			.getXLogger(PostprocessingTasklet.class);
    
	@Autowired
    private FileLogService fileLogService;
    
	@Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		
		//importLoggingService.updateBankImportLogOnFileImportComletion();
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return new ExitStatus("");
    }
}
