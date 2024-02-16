package io.github.batchservices.filepoller;

import io.github.batchservices.util.Constants;
import io.github.batchservices.util.FileUtility;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.util.Date;

public class FileMessageToJobRequest {

	private Job job;

	private String fileParameterName = "input.file.name";
	
	@Autowired
	private FileUtility fileUtility;
	
	public void setFileParameterName(String fileParameterName) {
		this.fileParameterName = fileParameterName;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Transformer
	public JobLaunchRequest toRequest(Message<File> message) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        updateJobParametersWithFileParameters(message.getPayload().getAbsolutePath(), jobParametersBuilder);        
		return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
	}

	private void updateJobParametersWithFileParameters(String filePath, JobParametersBuilder jobParametersBuilder) {
		File file = new File(filePath);
		String fileName = file.getName();	
		String runStreamId = fileName.substring(0,2);
		jobParametersBuilder.addString(Constants.RUNSTREAM_ID, runStreamId);
		jobParametersBuilder.addString(Constants.IS_ERROR_LOG_CREATED, "false");
		jobParametersBuilder.addString("file_path", filePath);
		jobParametersBuilder.addDate("date", new Date());
		String outputResource = filePath + "_IBPCS.TMP";
		jobParametersBuilder.addString("output_resource", outputResource);
	}
}
