package io.github.batchservices.service;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.global.Bank;
import io.github.batchservices.domain.global.BankExportLog;
import io.github.batchservices.domain.global.BankLog;
import io.github.batchservices.domain.global.BatchHeader;
import io.github.batchservices.domain.global.ErrorLog;
import io.github.batchservices.domain.global.FileLog;
import io.github.batchservices.domain.global.FileProperties;
import io.github.batchservices.exception.ApplicationException;
import io.github.batchservices.exception.FileException;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.repository.global.BankExportLogRepository;
import io.github.batchservices.repository.global.BankLogRepository;
import io.github.batchservices.repository.global.BankRepository;
import io.github.batchservices.repository.global.ErrorLogRepository;
import io.github.batchservices.repository.global.FileLogRepository;
import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.FileUtility;
import io.github.batchservices.util.ProcessStatus;
import io.github.batchservices.util.SeverityLevel;
import io.github.batchservices.util.Utils;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service("FileLogService")
public class FileLogService {

	@Autowired
	private FileLogRepository fileLogRepository;

	@Autowired
	private ErrorLogRepository errorLogRepository;

	@Autowired
	private BankLogRepository bankLogRepository;
	
	@Autowired
	BankExportLogRepository bankExportLogRepository;

	@Autowired
    ExecutionContextAccessor executionContextAccessor;
	
	@Autowired
	BankRepository bankRepository;
	
	@Autowired
    FileUtility fileUtility;

	private static ThreadLocal<SimpleDateFormat> FILE_DATE_FORMATTER = ThreadLocal.withInitial(() -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");        
        dateFormat.setLenient(false);
        return dateFormat;
    });
	
	private static final XLogger logger = XLoggerFactory
			.getXLogger(FileLogService.class);

	/*
	 * There are no pre-processing checks for Generic file transformer.
	 * As soon as a file is dropped, no matter whether it's a duplicate file or not, it's automatically ready 
	 * for processing. Hence we create a brand new File_Log record in file_log table with a default process_status_id=3 
	 * and begin processing Generic file.
	 * 
	 */
	public FileLog initializeFileLog(FileProperties fileProperties) throws Exception {	

		try {
			FileLog fileLog = new FileLog();
			fileLog.setFileTypeId(4); 
			fileLog.setDataTypeId(10);
			fileLog.setFileName(fileProperties.getFilePath());
		
			String shortFileName = new File(fileLog.getFileName()).getName();
			fileLog.setShortFileName(shortFileName);
			
			fileLog.setRunstreamId(fileProperties.getRunstreamId());
			fileLog.setFileSequenceTxt(fileProperties.getFileSequence());
			fileLog.setFileSize(fileProperties.getFileSize());
			fileLog.setPriority(1);
			fileLog.setReprocessType(0);

			java.sql.Date sqlFileDate = new java.sql.Date(fileProperties.getFileDate().getTime());
			fileLog.setFilePostDate(sqlFileDate);

			Timestamp cTime = new Timestamp(fileProperties.getFileCreationTime().toMillis());		
			fileLog.setFileCreateDate(cTime);

			Timestamp mTime = new Timestamp(fileProperties.getFileLastModifiedTime().toMillis());
			fileLog.setFileLastWriteDate(mTime);

			Timestamp aTime = new Timestamp(fileProperties.getFileLastAccessTime().toMillis());
			fileLog.setFileLastAccessDate(aTime);			
						
			fileLog.setProcessStatusId (ProcessStatus.PROCESSING.getValue());

			fileLog.setServerName(Utils.getLocalServerName());
			fileLog = fileLogRepository.create(fileLog);
			logger.debug( "New file_log entry - FileLogId: " + fileLog.getFileLogId() + 
					" Status: " + fileLog.getProcessStatusId() + " Priority: " + fileLog.getPriority());
			return fileLog;

		} catch (Exception e) {
			throw e;
		}	
	}

	public BankLog createBanklog(BatchHeader batchHeader) {
		try{
			Integer bankNumber = Utils.getBankNumberFromGenericBatchHeader(batchHeader);
			Map<Integer, Bank> bankMap = executionContextAccessor.getBankMap();		
			Bank bank = bankMap.get(bankNumber);
			if (bank == null){
				bank = bankRepository.findByBankNumber(bankNumber);
				bankMap.put(bankNumber, bank);			
			}
			Integer fileLogId = executionContextAccessor.getFileLogId();
			Map<Integer, BankLog> bankLogMap = executionContextAccessor.getBankLogMap();

			if (fileLogId ==null || bankMap ==null || bankLogMap == null){			
				logger.error(buildErrorString("createBanklog()", "Parameters missing in Execution Context")); 
				throw new ApplicationException("Parameters missing in Execution Context", BatchCode.APPLICATION_EXCEPTION);
			}

			BankLog bankLog = new BankLog();

			bankLog.setAsOfDate(FILE_DATE_FORMATTER.get().parse(batchHeader.getCompanyDescriptiveDate()))
			.setBankDomainName(bank.getBankDomainName())
			.setFileLogId(fileLogId)
			.setBatchSequenceTxt(String.valueOf(Integer.valueOf(batchHeader.getBatchNumber())))
			.setEffectiveDate(FILE_DATE_FORMATTER.get().parse(batchHeader.getEffectiveEntryDate()))
			.setProcessStatusId(ProcessStatus.PROCESSING.getValue())
			.setReprocessType(0);

			bankLog = bankLogRepository.create(bankLog);

			bankLogMap.put(bankNumber, bankLog);
			executionContextAccessor.setBankLogId(bankLog.getBankLogId());
			executionContextAccessor.setBankNumber(bankNumber);

			return bankLog;
		}
		catch (ApplicationException ae) {
			throw ae;
		}
		catch (Exception e){
			logger.error(buildErrorString("createBanklog()","Inserting Bank_Log record failed due to following exception: " + e.getMessage())); 
			throw new ApplicationException("Inserting Bank_Log record failed", BatchCode.APPLICATION_EXCEPTION);		
		}
	}

	public BankExportLog createBankExportLog(BatchHeader batchHeader, Integer bankLogId) {

		Integer fileLogId = executionContextAccessor.getFileLogId();		

		if (fileLogId ==null){ 
			throw new ApplicationException("createBankExportLog() - Parameters missing in Execution Context", BatchCode.APPLICATION_EXCEPTION);
		}

		BankExportLog bankExportLog = new BankExportLog();
		try{
			bankExportLog.setBankLogId(bankLogId)
			.setFileLogId(fileLogId)
			.setBankLogId(bankLogId)
			.setRecordCnt(0)
			.setCreateDate(FILE_DATE_FORMATTER.get().parse(batchHeader.getCompanyDescriptiveDate()))
			.setStartDate(FILE_DATE_FORMATTER.get().parse(batchHeader.getEffectiveEntryDate()))
			.setTotalCredit(new BigDecimal (100))
			.setTotalDebit(new BigDecimal (200));

			bankExportLogRepository.create(bankExportLog);
			return bankExportLog;
		}
		catch (Exception e){			 
			throw new ApplicationException("Inserting Bank_Export_Log record failed", BatchCode.APPLICATION_EXCEPTION);			
		}
	}
	
	public int updateBankExportLog(BankExportLog bankExportLog) {
		return (bankExportLogRepository.updateOnCompletion(bankExportLog));
	}
	
	public void updateBanklog(BankLog bankLog){
		bankLogRepository.updateOnCompletion(bankLog.getFileLogId(), bankLog.getBankLogId());

	}
	
	// Called when footer is reached.
	public void updateFileLogStatusOnCompletion(Integer fileLogId, Integer bachCount){	
		//String filePath = executionContextAccessor.getFilePath();
		String filePath = executionContextAccessor.getOutputResource();
		String suffix = filePath.substring(filePath.lastIndexOf('.') + 1);
		suffix = ( ! suffix.equals("TMP")) ? ("_IBPCS.DAT."+ suffix) : ("_IBPCS.DAT");		
		File customFormatFile = new File(filePath.substring(0, filePath.lastIndexOf("_IBPCS.TMP")) + suffix);		
		moveFile(new File(filePath), customFormatFile);		
		fileLogRepository.updateBatchCountAndStatusOnCompletion(fileLogId, customFormatFile.getAbsolutePath(), ProcessStatus.SUCCESSFUL.getValue(), bachCount);	
	}
	
	private void moveFile(File tmpFile, File datFile){
		if (tmpFile.exists()) {
			tmpFile.renameTo(datFile);			
		}
		else {
			logger.error ("NFT_FATAL_EXCEPTION - MOVE_FILE_ERROR in Post Processing - " + tmpFile.getName() + " Does not exist - TMP processing File: " + tmpFile + " - DAT converted File: " + datFile);
		}
	}

	// This is the ErrorLog object created for runtime exception
	private void insertErrorLog(Integer bankLogId, String errMessage, Integer fileLogId, Integer rowNumber){

		// Update error_log table  
		ErrorLog errorLog = new ErrorLog();
		try{			
			fileLogId = (fileLogId == null? 0 : fileLogId);
			bankLogId = (bankLogId == null? 0 : bankLogId);
			rowNumber = (rowNumber == null? 0 : rowNumber);

			errorLog.setFileLogId(fileLogId);
			errorLog.setBankLogId(bankLogId);
			errorLog.setErrMessage(errMessage);
			errorLog.setSeverityLevelId(SeverityLevel.FATAL.getValue());
			errorLog.setRecordNum(rowNumber);

			if (errorLogRepository == null) {
				logger.error("FileLogId: " + fileLogId + " - ErrorLogRepository has not been initialized.");
			} else {
				// Take the exception and persist into error_log table				
				errorLogRepository.insertErrorLog(errorLog);
			}
		}
		catch (Exception e){
			logger.error("ERROR_LOG_TABLE - FAILED while inserting Fatal Exception detail record.");			
		}
	}

	/***
	 * This is the central place to log necessary details in response to a fatal exception.
	 * 	 * 1) A fatal exception could be raised as checked (or handled) exception from anywhere in a spring batch step 
	 * such as a Reader, a Writer or a Processor or any other component/service that is used by these components.
	 * 	 * 2) A fatal exception could be an un-checked exception as well that is thrown during run-time.
	 * In both of the above cases, below code shall be called which will grab FILE_LOG_ID, BANK_LOG_ID and ROW_NUM from the 
	 * job execution context and will perform necessary logging.  
	 */

    private void handleFatalException(String eventOrigin, Exception ex) {
        handleFatalException(eventOrigin, "", ex);
    }

	private void handleFatalException(String eventOrigin, String customErrorMessage, Exception ex) {

		Integer fileLogId = executionContextAccessor.getFileLogId() == null ? 0 : executionContextAccessor.getFileLogId();
		Integer bankLogId = executionContextAccessor.getBankLogId() == null ? 0 : executionContextAccessor.getBankLogId();
		Integer rowNumber = executionContextAccessor.getRowNum();

		try{
			logger.debug("FATAL_EXCEPTION_LOGGING_BEGIN - Attempting to update following tables as a result of Fatal Exception : 'Error_log', 'file_log' and 'bank_log'");

			// 1. Log to application log file
            if(customErrorMessage.isEmpty())
			    logger.error(buildErrorString(eventOrigin, (ex.getMessage()==null? "NullPointerException" : ex.getMessage())));
            else
                logger.error(customErrorMessage +" - "+ buildErrorString(eventOrigin, (ex.getMessage()==null? "NullPointerException" : ex.getMessage())));

			if (fileLogId > 0){
				String shortErrMessage = customErrorMessage.isEmpty() ? getShortErrorMessage(ex) : customErrorMessage;

				// 2. Log into error_log
				insertErrorLog(bankLogId, shortErrMessage, fileLogId, rowNumber);
				logger.debug("ERROR_LOG_TABLE - SUCCESS - Fatal Exception detail record inserted.");
			
				// 3. Update process_status_id to 7 in file_log				
				fileLogRepository.updateOnCompletion(fileLogId,ProcessStatus.FATAL.getValue());	
				logger.debug("FILE_LOG_TABLE - SUCCESS - Fatal Exception detail record inserted.");
				
				// 4. Update bank_log status	
				// NOTE: If the exception was thrown before we got to the Batch Header, there will be no Bank_log_id.
				if (bankLogId > 0){
					// Update process_status_id to 7 in bank_log
					bankLogRepository.updateOnCompletion(fileLogId, bankLogId, ProcessStatus.FATAL.getValue());
					logger.debug("BANK_LOG_TABLE - SUCCESS - Fatal Exception detail record inserted.");
				}
				else{
					//if (shortErrMessage.contains("FILE HEADER VALIDATION")) // Bank Log Id is not available when failure happened during File Header validation.
					//	logger.debug ("BANK_LOG_TABLE - NOT UPDATED. Could not update Bank_Log table with completion status - \"bankLogId\" field was not set.");
					//else
					logger.debug ("FATAL_EXCEPTION_LOGGING - could not log into bank_log table. No bank_log_id.");
				}
			} else {
				// When Spring Batch gets past "Pre-processing validation", it MUST have fileLogId in job context.
				// If it did not, then it should have failed there it-self and job would have been aborted. 
				// It must be some other error if fileLogId is not available here.
				// NOTE: If called in Pre-Processing step, before File_Log entry is created, there will not be any File_Log_Id.
				logger.debug ("FATAL_EXCEPTION_LOGGING - could not log into File_Log tables Exception occurred. No file_log_id.");

				// Exit from the function as you cannot write into any of the tables.
				return;
			}

		} catch (Exception e){ 
			// 5. If any exception while logging into tables, insert into Application log.
			String errorMessage = "FATAL_EXCEPTION_LOGGING_FAILED - Failed to insert 'Fatal Exception Details' into one (or all) of the following tables 'Error_Log', 'file_log' or 'bank_log', due to following exception - " + e.getMessage();
			String origin = "FATAL_EXCEPTION_LOGGING";		
			logger.error(buildErrorString(origin, errorMessage));
			return;
		}

		logger.debug("FATAL_EXCEPTION_LOGGING_COMPLETED - Completed updating following tables as a result of Fatal Exception : 'Error_log', 'file_log' and 'bank_log'");

	}

	private String buildErrorString(String eventOrigin, String errMessage) {

		// Get file-name from Job Context
		String fileName = executionContextAccessor.getFilePath();

		// Get filelLogId
		Integer fileLogId = executionContextAccessor.getFileLogId();

		// Get bankLog
		Integer bankNumber = executionContextAccessor.getBankNumber();
		Map<Integer, BankLog> bankLogMap = executionContextAccessor.getBankLogMap();
		BankLog bankLog = (bankLogMap!=null ? bankLogMap.get(bankNumber) : null);

		// Get bankLogId
		Integer bankLogId = (bankLog!=null ? bankLog.getBankLogId() : null);

		StringBuilder builder = new StringBuilder();
		builder.append("NFT_FATAL_EXCEPTION : ")
		.append(eventOrigin)
		.append(" - [File Name: \"")
		.append(fileName)
		.append("\" | File Log Id: \"")
		.append(fileLogId)
		.append("\" | Bank Log Id: \"")
		.append(bankLogId)  // .append(bankLogId +" \" ] | ")
		.append("\" | NACHA file line num: \"")
		.append(executionContextAccessor.getRowNum() +" \" ] | ")		
		.append(errMessage);

		return (builder.toString());

	}

	private String getShortErrorMessage(Exception ex){

		String errorLogTableErrMessage = null;

		try{

			Throwable parentException = ex;

			while (parentException.getCause() != null){
				parentException = parentException.getCause();
			}

			errorLogTableErrMessage = parentException.getMessage();

			if (errorLogTableErrMessage == null){
				if (parentException instanceof NullPointerException)
					errorLogTableErrMessage = "NullPointerException";
				else
					errorLogTableErrMessage = "Could not retrieve Error Message from Exception object";
			}

			if (errorLogTableErrMessage.length() > 255){
				errorLogTableErrMessage = errorLogTableErrMessage.substring(0, 255);
			}

		}catch (Exception e){			
			logger.error("Could not retrieve Error Message from Exception object to log into \"Error_log\" table: "+ e.getMessage());
			errorLogTableErrMessage = "Could not retrieve Error Message from Exception object";			
		}
		return errorLogTableErrMessage;	
	}
	
	// Client facing function for Central Exception Handler - called by onReadError and onProcessError (Reader and Processor) 
	public void handleOnErrorEvent(String eventOrigin, Exception ex, EntityMarker entity) {
		handleOnErrorEvent(eventOrigin, "", ex, entity);
	}

	public void handleOnErrorEvent(String eventOrigin, String customErrorMessage, Exception ex, EntityMarker entity) {
		if (executionContextAccessor.getGlobalExceptionCount() < 1){
			// insertErrorLog(entity); // For NFT we don't store errors in entities. 
			handleFatalException(eventOrigin, customErrorMessage, ex);
		}
		if (ex instanceof FileException || ex instanceof ApplicationException) {
			renameFileToCorrupt();
		}
		if (executionContextAccessor.getGlobalExceptionCount() >= 1){
			// insertErrorLog(entity); // For NFT we don't store errors in entities.
			handleFatalException(eventOrigin, ex);
		}
	}

	// Client facing function for Central Exception Handler - called by onWriteError (Writer) 
	public void handleOnErrorEvent(String eventOrigin, Exception ex, List<? extends EntityMarker> list){
		if (executionContextAccessor.getGlobalExceptionCount() < 1){
			// insertErrorLog(list); 	// For NFT we don't store errors in entities.
			handleFatalException(eventOrigin, ex);
		}
		if (ex instanceof FileException) {
			renameFileToCorrupt();
		}
		if (executionContextAccessor.getGlobalExceptionCount() >= 1){
			// insertErrorLog(list);		// For NFT we don't store errors in entities.
			handleFatalException(eventOrigin, ex);
		}
	}

	// Client facing function for Central Exception Handler - (All others) for other components such as Task-lets which does not have an "entity" to pass as a parameter.
	public void handleOnErrorEvent(String eventOrigin, Exception ex){
		handleFatalException(eventOrigin, ex);
		if (ex instanceof FileException) {
			renameFileToCorrupt();
		}
	}

	// Client facing function for GlobalExceptionHandlerAspect
	// Sravan V: Note that earlier logic thrown exception from within this function, which caused recursive exception hander calls. 
	// That was a major issue. The main logic in handling Fatal runtime exceptions is following. Just increase getGlobalExceptionCount
	// in this function and just leave it there. Processor's onError callback function gets called soon after the execution of this function
	// which will perform fatal exception handling as per normal course by calling "handleOnErrorEvent(String eventOrigin, Exception ex, EntityMarker entity)" function. 
	public void handleOnErrorEventRaiseException(String eventOrigin, Exception ex){
		executionContextAccessor.setGlobalExceptionCount(executionContextAccessor.getGlobalExceptionCount() +1);
	}

//	private void prepareErrorLog(EntityMarker entity, List<ErrorLog> errorLog){		
//		AbstractEntity abstractEntity = (AbstractEntity) entity;		
//        // Prepare errorLog list for the current entity
//		if (!abstractEntity.getErrorLog().isEmpty()) {
//			errorLog.addAll(abstractEntity.getErrorLog());
//		}		
//	}
	
	private void renameFileToCorrupt() {		
		String filePath = executionContextAccessor.getOutputResource();
		File tmpFile = new File(filePath);
		File corrputFile = new File(filePath + ".CORRUPT");
		moveFile(tmpFile, corrputFile);
	}
}
