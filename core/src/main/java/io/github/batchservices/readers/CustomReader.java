package io.github.batchservices.readers;

import io.github.batchservices.domain.AbstractDetailRecord;
import io.github.batchservices.util.BatchCode;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.context.RepeatContextSupport;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.global.BatchHeader;
import io.github.batchservices.domain.global.FileFooter;
import io.github.batchservices.exception.FileException;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.service.FileLogService;

/**
 * 1) Generic CustomItemReader that needs to be configured BOTH as reader AND as
 * chunk-completion-policy to be able to update its internal state.
 *  
 * 2) This can be used in all file types. Each file type should have it's own "FlatFileItemReader" configured with necessary
 * Line Mappers, Field Set Mappers, and Line Tokenizers. The corresponding "FlatFileItemReader" of a given file import
 * should be passed as a delegate to "CustomReader".  
 */

// Do not annotate with Spring @Component. Instead create StepScope bean inside CustomerConfig class
public class CustomReader extends SimpleCompletionPolicy implements ItemReader<EntityMarker>,  CompletionPolicy, ItemReadListener<EntityMarker> {

	@Autowired
	private FileLogService fileLogService;

	@Autowired
	ExecutionContextAccessor executionContextAccessor;

	private EntityMarker currentRepeatContextEntity;

	private EntityMarker currentEntity;

	private int rowNumber=0;
	
	private int totalProcessed;
	
	private Integer fileLogId;

	private Exception fatalException = null;

	FlatFileItemReader<EntityMarker> customItemReader;

	SingleItemPeekableItemReader<EntityMarker> peekable;

	private static final XLogger logger = XLoggerFactory
			.getXLogger(CustomReader.class);

	public void setCustomItemReader(FlatFileItemReader<EntityMarker> customItemReader) {
		this.customItemReader = customItemReader;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		peekable = new SingleItemPeekableItemReader<EntityMarker>();
		peekable.setDelegate(customItemReader);
		peekable.open(stepExecution.getExecutionContext());
		fileLogId = executionContextAccessor.getFileLogId();
	}

	@AfterStep
	public void afterStep(StepExecution stepExecution) {
		peekable.close();
	}

	@Override
	public EntityMarker read() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		
		rowNumber++;		
		executionContextAccessor.setRowNum(rowNumber);

		// Before reading, check if there is any fatal exception thrown by peak() method
		if (fatalException != null)
			throw (fatalException);

		currentEntity = peekable.read();
		AbstractEntity currentRecord = (AbstractEntity) currentEntity;

		if (currentRecord != null){
			currentRecord.setFileLogId(fileLogId);
			currentRecord.setRowNumber(rowNumber);
		}
		
		return (EntityMarker) currentRecord;
	}

	@Override
	public boolean isComplete(RepeatContext context) {
		return ((ReaderRepeatContext) context).isComplete();
	}

	@Override
	public boolean isComplete(RepeatContext context, RepeatStatus result) {
		return ((ReaderRepeatContext) context).isComplete();
	}

	@Override
	public RepeatContext start(RepeatContext parent) {

		// Set first item of the chunk for later comparison.		
		totalProcessed = 0;
		EntityMarker object = invokePeek();

		if ((AbstractEntity)object instanceof AbstractDetailRecord)
			this.currentRepeatContextEntity = object;

		return new ReaderRepeatContext(parent);
	}

	@Override
	public void update(RepeatContext context) {
		/*
		 * Check if the step should finish.
		 * In this case when there are no more records to process.
		 */
		totalProcessed++;
		if (currentRepeatContextEntity == null) {			
			context.setCompleteOnly();
		}
	}

	private EntityMarker invokePeek() {
		EntityMarker peeked = null;	
		try {
			peeked = peekable.peek();

		} catch (Exception e) {			
			logger.error("FATAL EXCEPTION THROWN in invokePeek()");			
			fatalException = e;			
		}
		return peeked;
	}

	//custom RepeatContext
	protected class ReaderRepeatContext extends RepeatContextSupport {

		public ReaderRepeatContext(RepeatContext parent) {
			super(parent);
		}

		public boolean isComplete() {

			EntityMarker peakedEntity = null;
			peakedEntity = invokePeek();

			if(currentEntity instanceof FileFooter || 
					peakedEntity instanceof FileFooter || 
					peakedEntity instanceof BatchHeader || 
					currentEntity == null) {
				return true;
			}
			else return false;
		}
	}

	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRead(EntityMarker item) {
		// TODO Auto-generated method stub

	}

	private static final String INVALID_RECORD_TYPE = "Invalid Record Type ";
	@Override
	public void onReadError(Exception ex) {
		logger.error("Callback received by 'onReadError' method. Redirecting to 'fileLogService.handleFatalException()' to do necessary logging");
		if (ex instanceof FileException || ex.getCause() instanceof FileException) {
			fileLogService.handleOnErrorEvent("CustomReader", ex, currentEntity);
		} else {
			fileLogService.handleOnErrorEvent("CustomReader", new FileException(INVALID_RECORD_TYPE, BatchCode.FILE_VALIDATION_FAILURE));
		}

	}
}
