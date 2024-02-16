package io.github.batchservices.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StepNotificationListener implements StepExecutionListener {

    private static Logger logger = LoggerFactory.getLogger(StepNotificationListener.class);

    @Autowired
	private ExecutionContextAccessor executionContextAccessor;
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

    	ExitStatus exitStatus = stepExecution.getExitStatus();
		String exitStatusCode = stepExecution.getExitStatus().getExitCode();
		String exitStatusDesc = stepExecution.getExitStatus().getExitDescription();
		
		if (ExitStatus.FAILED.getExitCode().equals(exitStatusCode))  {
			logger.error("[" + stepExecution.getStepName() +"]" + "- !!! AFTER STEP !!! - " + executionContextAccessor.getFilePath() + " - Import Failed with exit status: " + exitStatus);    
		} else if (ExitStatus.STOPPED.getExitCode().equals(exitStatusCode)) {
		    logger.info("[" + stepExecution.getStepName() +"]" + "- !!! AFTER STEP !!! - " + executionContextAccessor.getFilePath() + " - Import Stopped with exit status: " + exitStatus);
		} else if (ExitStatus.COMPLETED.getExitCode().equals(exitStatusCode)) {
		    logger.debug ("[" + stepExecution.getStepName() +"]" + "- !!! AFTER STEP !!! - " + executionContextAccessor.getFilePath() + " - Import Completed Successfully !!!");
		} else {
		    logger.debug("[" + stepExecution.getStepName() +"]" + "- !!! AFTER STEP !!! - " + executionContextAccessor.getFilePath() + " - Step completed with exit status: " + exitStatus);            
		}		

        return exitStatus;
    }
}
	

