package io.github.batchservices.processor;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.batchservices.domain.global.FileHeader;
import io.github.batchservices.service.FileLogService;

// NOTE: Do not annotate with Spring @Component. Instead create StepScope bean inside CustomerConfig class
public class TransactionsItemProcessor extends AbstractItemProcessor implements ItemProcessor<EntityMarker, EntityMarker>, ItemProcessListener<EntityMarker, EntityMarker>  {

	StepExecution stepExecution;
	
	@Autowired
	FileLogService fileLogService;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public void beforeProcess(EntityMarker entity) {
		
	}

	@Override
	public void afterProcess(EntityMarker entity, EntityMarker entity2) {

	}

	@Override
	public void onProcessError(EntityMarker entity, Exception e) {
		fileLogService.handleOnErrorEvent("NachaFileItemProcessor", e, entity);
		stepExecution.setExitStatus(ExitStatus.FAILED);
	}
	
	@Override
	public EntityMarker process(EntityMarker entity) throws Exception {
		
		if ((AbstractEntity) entity instanceof FileHeader) {
			// YYMMDD
			String natchaFileCreationDate = Integer.toString(((FileHeader) entity).getFileCreationDate());
			// Convert this to MMDDCCYY
			String customDate = natchaFileCreationDate.substring(2, 4) + natchaFileCreationDate.substring(4, 6) + "20" + natchaFileCreationDate.substring(0, 2); 
			stepExecution.getJobExecution().getExecutionContext().put("header", customDate);
		}	
		return doProcess(entity);		
	}
}
