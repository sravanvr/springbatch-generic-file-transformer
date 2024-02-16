package io.github.batchservices.listeners;

import io.github.batchservices.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

import io.github.batchservices.domain.global.Bank;
import io.github.batchservices.domain.global.BankLog;
import io.github.batchservices.domain.global.CustomTranCodeMappings;
import io.github.batchservices.domain.global.FileLog;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/***
 * DO NOT annotate this class as Spring Component or Service. The corresponding bean for this class is created in CustomerConfig.java class 
 * with "Step Scope" and will be registered in the step as the listener.
 * Auto-wire this class as below:
 * @Autowired
   ExecutionContextAccessor executionContextAccessor;
 */

// DO NOT annotate this class as Spring Component or Service, rather create a "Step Scope'd" bean and register this class as a listenet to
// the step where you want to have centralized access to Job Execution context.
public class ExecutionContextAccessor implements StepExecutionListener{

	StepExecution stepExecution;

	private static Logger logger = LoggerFactory.getLogger(ExecutionContextAccessor.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	public void clearExecutionContext(){
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		for (Entry<String, Object> entry : executionContext.entrySet()) {
			String key = entry.getKey();
			executionContext.remove(key);
		}
	}
	
	/*** Setters ****/
	public void setFileLog(FileLog fileLog){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.FILE_LOG, fileLog);
	}

	public void setFileLogId(Integer fileLogId){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.FILE_LOG_ID, fileLogId);
	}	

	public void setBankLogId(Integer bankLogId){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.BANK_LOG_ID, bankLogId);
	}
	
	public void setRowNum(Integer rowNum){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.ROW_NUM, rowNum);
	}

	public void setBankNumber(Integer bankNumber){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.BANK_NUM, bankNumber);
	}

	public void setBankMap(Map<Integer, Bank> bankMap){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.BANK_MAP, bankMap);
	}

	public void setBankLogMap(HashMap<Integer, BankLog> bankLogMap){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.BANK_LOG_MAP, bankLogMap);
	}
	
	public void setCustomTranCodeMap(Map<Integer, CustomTranCodeMappings> customTranCodeMapMap){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.TRANS_CODES_MAP, customTranCodeMapMap);
	}
	
	public void setCustomFormatHeader(String customFormatHeaderWriter) {
		stepExecution.getJobExecution().getExecutionContext().putString("CUSTOM_FORMAT_HEADER", customFormatHeaderWriter);
	}

	public void setErrorLogInsertStatus(String status){
		stepExecution.getJobExecution().getExecutionContext().putString(Constants.ERROR_LOG_INSERT_STATUS, status);
	}

	public void setFileSequence(String sequence){
		stepExecution.getJobExecution().getExecutionContext().putString(Constants.IMPORT_FILE_SEQUENCE, sequence);
	}
	
	public void setFileType(Integer fileTypeId){
		stepExecution.getJobExecution().getExecutionContext().putInt(Constants.FILE_TYPE, fileTypeId);
	}
	
	public void setFileNameWithOutExtension(String fileName){
		stepExecution.getJobExecution().getExecutionContext().putString(Constants.IMPORT_FILE_NAME_WITHOUT_EXT, fileName);
	}

	public void setFilePostDate(Date filePostDate){
		stepExecution.getJobExecution().getExecutionContext().put(Constants.FILE_POSTING_DATE, filePostDate);
	}

	public void setGlobalExceptionCount(Integer globalExceptionCount){
		stepExecution.getJobExecution().getExecutionContext().putInt(Constants.GLOBAL_EXCEPTION_COUNT, globalExceptionCount);
	}
	
	public void setOutputResource(String outputResource){
		stepExecution.getJobExecution().getExecutionContext().put("OUTPUT_RESOURCE", outputResource);
	}
	
	/***
	 * Getters
	 */
	
	public String getFilePath(){
		String fileName = null;
		try{
			fileName = stepExecution.getJobExecution().getJobParameters().getString("file_path");
		}
		catch (Exception e){
		}
		return fileName;
	}
	
	public FileLog getFileLog(){
		FileLog fileLog = null;
		try{
			fileLog = (FileLog) stepExecution.getJobExecution().getExecutionContext().get(Constants.FILE_LOG);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"FILE_LOG\" has not been set in the context - %s", e.getMessage());
		}
		return fileLog;
	}

	public Integer getFileLogId(){
		Integer fileLogId = null;
		try{
			fileLogId = stepExecution.getJobExecution().getExecutionContext().getInt(Constants.FILE_LOG_ID);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"FILE_LOG_ID\" has not been set in the context - %s", e.getMessage());
		}
		return fileLogId;
	}
	
	public Integer getRowNum(){
		Integer rowNum = null;
		try{
			rowNum = stepExecution.getJobExecution().getExecutionContext().getInt(Constants.ROW_NUM);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"ROW_NUM\" has not been set in the context - %s", e.getMessage());
		}
		return rowNum;
	}

	public Integer getBankNumber(){
		Integer bankNumber = 0;
		try{
			bankNumber = stepExecution.getJobExecution().getExecutionContext().getInt(Constants.BANK_NUM);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"BANK_NUM\" has not been set in the context - %s", e.getMessage());
		}
		return bankNumber;
	}
	
	public Integer getBankLogId(){
		Integer bankLogId = 0;
		try{
			bankLogId = stepExecution.getJobExecution().getExecutionContext().getInt(Constants.BANK_LOG_ID);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"BANK_LOG_ID\" has not been set in the context - %s", e.getMessage());			
		}
		return bankLogId;
	}

	public Map<Integer, Bank> getBankMap(){

		Map<Integer, Bank> bankMap = null;
		try{
			bankMap = (Map<Integer, Bank>)stepExecution.getJobExecution().getExecutionContext().get(Constants.BANK_MAP);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"BANK_MAP\" has not been set in the context - %s", e.getMessage());
		}
		return bankMap;
	}

	public Map<Integer, BankLog> getBankLogMap(){

		Map<Integer, BankLog> bankLogMap = new HashMap<>();
		try{
			bankLogMap = (Map<Integer, BankLog>) stepExecution.getJobExecution().getExecutionContext().get(Constants.BANK_LOG_MAP);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"BANK_LOG_MAP\" has not been set in the context - %s", e.getMessage());
		}
		return bankLogMap;
	}

	public Map<Integer, CustomTranCodeMappings> getCustomTranCodeMap(){

		Map<Integer, CustomTranCodeMappings> customTranCodeMappingsMap = null;
		try{
			customTranCodeMappingsMap = (Map<Integer, CustomTranCodeMappings>)stepExecution.getJobExecution().getExecutionContext().get(Constants.TRANS_CODES_MAP);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"BANK_MAP\" has not been set in the context - %s", e.getMessage());
		}
		return customTranCodeMappingsMap;
	}
	
	public Integer getGlobalExceptionCount() {
		Integer globalExceptionCount = 0;
		try{
			globalExceptionCount = stepExecution.getJobExecution().getExecutionContext().getInt(Constants.GLOBAL_EXCEPTION_COUNT);
		}
		catch (Exception e){
			logger.debug("EXECUTION_CONTEXT - \"GLOBAL_EXCEPTION_COUNT\" is not initialized yet");
			return 0;
		}		
		return globalExceptionCount;
	}
	
	public String getErrorLogInsertStatus(){
		String status = null;
		try{
			status = stepExecution.getJobExecution().getExecutionContext().getString(Constants.ERROR_LOG_INSERT_STATUS);
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"ERROR_LOG_INSERT_STATUS\" has not been set in the context - %s", e.getMessage());
		}
		return status;
	}
	
	public String getOutputResource(){
		String status = null;
		try{
			status = stepExecution.getJobExecution().getExecutionContext().getString("OUTPUT_RESOURCE");
		}
		catch (Exception e){
			logger.error("EXECUTION_CONTEXT - \"OUTPUT_RESOURCE\" has not been set in the context - %s", e.getMessage());
		}
		return status;
	}	

	public Integer getFileType() {
		return stepExecution.getJobExecution().getExecutionContext().getInt(Constants.FILE_TYPE);
	}
}
