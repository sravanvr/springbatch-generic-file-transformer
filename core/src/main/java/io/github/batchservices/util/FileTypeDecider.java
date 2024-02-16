package io.github.batchservices.util;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;
import static io.github.batchservices.util.Constants.NACHA_TRANSACTIONS_FILE;

/***
 * 
 * @author rv250129
 *
 */

@Component
public class FileTypeDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
    	
//        //Map<String, JobParameter> jobParameters = jobExecution.getJobParameters().getParameters();
//        ExecutionContext context = jobExecution.getExecutionContext();
//
//        Integer fileVersion = context.getInt(BATCH_FILE_VERSION);
//        //String fileType = jobParameters.get(FILE_TYPE).toString() + "_V" + fileVersion.toString();
//        String fileType = FileType.values()[Integer.valueOf(context.getInt(FILE_TYPE))-1].name() 
//        						+ "_V" + fileVersion.toString();
        
        String fileType  = NACHA_TRANSACTIONS_FILE;
        return new FlowExecutionStatus(fileType);
    }

}
