package io.github.batchservices.tasklets;

import io.github.batchservices.domain.global.Bank;
import io.github.batchservices.domain.global.BankLog;
import io.github.batchservices.domain.global.FileLog;
import io.github.batchservices.domain.global.FileProperties;
import io.github.batchservices.exception.ApplicationException;
import io.github.batchservices.exception.FileException;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.repository.global.BankRepository;
import io.github.batchservices.repository.global.CustomTranCodeMappingsRepository;
import io.github.batchservices.service.FileLogService;
import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.FileUtility;

import org.slf4j.MDC;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/***
 * 
 * @author rv250129
 * There is no duplicate file concept for NACHA Transformer. Every file that is dropped inside "/app/export/read/"
   will be processed even if it's dropped multiple times. Once a file is dropped it's automatically ready for processing. 
   There are no additional processing eligibility checks.
 */

public class PreprocessingTasklet implements Tasklet {

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private FileLogService fileLogService;

	@Autowired
	private ExecutionContextAccessor executionContextAccessor;

	@Autowired
	private FileUtility fileUtility;

	@Autowired
	CustomTranCodeMappingsRepository customTranCodeMappingsRepository;
	
	private String filePath;
	private String outputResource;
	
	public PreprocessingTasklet(String filePath) {
		this.filePath = filePath;
	}

	private static ThreadLocal<SimpleDateFormat> FILE_DATE_FORMATTER = ThreadLocal.withInitial(() -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");        
        dateFormat.setLenient(false);
        return dateFormat;
    });
	
	private static final XLogger logger = XLoggerFactory
			.getXLogger(PreprocessingTasklet.class);
	
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

		FileProperties fileProperties;
		FileLog fileLog;		
		executionContextAccessor.clearExecutionContext();		

		JobParameters jobParameters = chunkContext.getStepContext()
				.getStepExecution()
				.getJobExecution()
				.getJobParameters();

		outputResource = jobParameters.getString("output_resource");
		
		try {

			MDC.put("filePath", filePath);

			if (filePath.isEmpty() || filePath == null || ! fileUtility.doesFileExist(filePath)){
				String errorMessage = String.format("FilePath is either empty or not available. Aborting NFT.");
				return (handleTaskletFailure(stepContribution, errorMessage, BatchCode.FILE_NOT_FOUND));
			}
			
			fileProperties = setFileProperties();
			fileLog = fileLogService.initializeFileLog(fileProperties);

			MDC.put("fileLogId", Integer.toString(fileLog.getFileLogId()));
		    logger.info( "FileLogId: " + fileLog.getFileLogId());
		    setExecutionContextProperties(fileLog);

		    // Verify File Footer presence
		    if (! isFileFooterPresent(stepContribution)){								
				String errorMessage = String.format("Incomplete file: %s, Missing File Footer. File rejected.", filePath);				
				return (handleTaskletFailure(stepContribution, errorMessage, BatchCode.FILE_VALIDATION_FAILURE));
			}
		    
		    // Verify File Header presence
			String customHeader = getCustomFileHeader(fileProperties.getFileDate());
			if (customHeader != null){
				chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("header", customHeader);
			}
			else{
				String errorMessage = String.format("Incomplete file: %s, Missing File Header. File rejected.", filePath);
				return (handleTaskletFailure(stepContribution, errorMessage, BatchCode.FILE_VALIDATION_FAILURE));
			}
			
			executionContextAccessor.setCustomTranCodeMap(customTranCodeMappingsRepository.getTransCodes());

			stepContribution.setExitStatus(ExitStatus.COMPLETED);

		} catch (Exception e) {
			fileLogService.handleOnErrorEvent(e.getClass().getSimpleName(), e);
		}
		return RepeatStatus.FINISHED;
	}

	private RepeatStatus handleTaskletFailure(StepContribution stepContribution, String errorMessage, BatchCode batchCode){		
		logger.error(errorMessage);
		stepContribution.setExitStatus(ExitStatus.FAILED);  	
		if (batchCode == BatchCode.FILE_VALIDATION_FAILURE) {
			fileLogService.handleOnErrorEvent("PreProcessing Tasklet", new FileException(errorMessage, batchCode));
		} else {
			fileLogService.handleOnErrorEvent("PreProcessing Tasklet", new ApplicationException(errorMessage, batchCode));
		}
		return RepeatStatus.FINISHED;
	}

	// Set Execution Context using the central class	
	private void setExecutionContextProperties(FileLog fileLog){				
		executionContextAccessor.setFileSequence(fileLog.getFileSequenceTxt());
		executionContextAccessor.setFileType(fileLog.getDataTypeId());
		executionContextAccessor.setFileLogId(fileLog.getFileLogId());
		executionContextAccessor.setFileLog(fileLog);
		executionContextAccessor.setBankMap(new HashMap<Integer, Bank>());
		executionContextAccessor.setBankLogMap(new HashMap<Integer, BankLog>());
		executionContextAccessor.setFileNameWithOutExtension(fileLog.getShortFileName());
		executionContextAccessor.setOutputResource(outputResource);
	}
	
	// Since this is try-with-resources statement it gets automatically closed. No need to explicitly close BufferReader.
	private String getFirstLine(File importFile) throws IOException{
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(importFile))) {
			return bufferedReader.readLine();
		}
	}

	private String getCustomFileHeader(Date fileDate) throws Exception{
		File nachaFile = new File(filePath);
		String firstLine = getFirstLine(nachaFile);			    	
		if (firstLine.charAt(0) != '1'){ // i.e. file header is missing			
			return null;
		}
		else{
			return (getCustomHeader(firstLine, fileDate));			
		}
	}
	
	// nachaHeader is in YYMMDD format. Convert this to MMDDCCYY.
	private String getCustomHeader(String nachaHeader, Date fileDate){
		nachaHeader = nachaHeader.substring(23,29);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fileDate);
		nachaHeader  = nachaHeader.substring(2, 4) + nachaHeader.substring(4, 6) + String.valueOf(cal.get(Calendar.YEAR)).substring(2) + nachaHeader.substring(0, 2);		
		return (nachaHeader);
	}
	
	private FileProperties setFileProperties () throws IOException, ParseException {	
		FileProperties fileProperties = new FileProperties();
		try {
			Path path = Paths.get(filePath);			
			File file = new File(filePath);
			String fileName = file.getName();

			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

			fileProperties.setFilePath(filePath);
			fileProperties.setFileName(fileName);

			String fileNameWithOutExtension = fileName;
			int idot = fileName.lastIndexOf(".TMP");
			if (idot != -1) {
				// Remove any extension with dot
				fileNameWithOutExtension = fileName.substring(0, idot);	
				// Remove suffix _IBPCS
				fileNameWithOutExtension = fileNameWithOutExtension.substring(0, fileName.lastIndexOf("_IBPCS"));	
			}
	
			int countUnderscores = StringUtils.countOccurrencesOf(fileNameWithOutExtension, "_");
			
			fileProperties.setRunstreamId(fileNameWithOutExtension.substring(0,2));			
			if (countUnderscores == 0) {		
				fileProperties.setFileSequence(fileNameWithOutExtension.substring(2,3));
				fileProperties.setDataTypeId(fileNameWithOutExtension.substring(3,4));				
				fileProperties.setFileMM(fileNameWithOutExtension.substring(4,6));
				fileProperties.setFileDD(fileNameWithOutExtension.substring(6,8));
			} else {
				throw new FileException("Unrecognizable format for filename: " + fileName, BatchCode.FILE_REJECTED);				
			} // else block ends here
			
			fileProperties.setFileDate(getFileDateFromNachaFileName(fileProperties, attrs));			
			fileProperties.setFileCreationTime(attrs.creationTime());
			fileProperties.setFileLastAccessTime(attrs.lastAccessTime());
			fileProperties.setFileLastModifiedTime(attrs.lastModifiedTime());
			fileProperties.setFileSize(file.length());

		} catch (IOException e) {
			throw (e);
		} catch (ParseException e) {
			throw (e);
		}

		return fileProperties;		
	}
	
    // This helper function deals with Nacha file's fileDate which is in "MM/dd/yyyy" format.
	// "fileDate" property of "FileProperties" object holds a date that is captured from "nacha file name" using 4th to 8th characters. 
	private Date getFileDateFromNachaFileName(FileProperties fileProperties, BasicFileAttributes attrs) throws ParseException{		
		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);
		String strFileDate = fileProperties.getFileMM() + "/" + fileProperties.getFileDD() + "/" + currentYear;	
		Date fileDate = FILE_DATE_FORMATTER.get().parse(strFileDate);
		Date curDate = new Date();
		if (fileDate.compareTo(curDate) > 0) {				
			cal.setTime(FILE_DATE_FORMATTER.get().parse(FILE_DATE_FORMATTER.get().format(attrs.creationTime().toMillis())));				
			int yearOfFileCreationDate = cal.get(Calendar.YEAR);
			if (yearOfFileCreationDate < currentYear){
				strFileDate = fileProperties.getFileMM() + "/" + fileProperties.getFileDD() + "/" + yearOfFileCreationDate;
			}
			else{
				strFileDate = fileProperties.getFileMM() + "/" + fileProperties.getFileDD() + "/" + (currentYear - 1);	
			}				
			fileDate = FILE_DATE_FORMATTER.get().parse(strFileDate);
		}	
		return fileDate;
	}
	
	private boolean isFileFooterPresent(StepContribution stepContribution) throws Exception{
		File importFile = new File(filePath);
		String lastLine = tail(importFile, stepContribution);    	
		return ( (lastLine.charAt(0) != '9') ? false : true );
	}
	
	// Gets the last line of given file.
	private String tail( File file, StepContribution stepContribution ) {
		RandomAccessFile fileHandler = null;
		try {
			fileHandler = new RandomAccessFile( file, "r" );
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();

			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				if( readByte == 0xA ) {
					if( filePointer == fileLength ) {
						continue;
					}
					break;

				} else if( readByte == 0xD ) {
					if( filePointer == fileLength - 1 ) {
						continue;
					}
					break;
				}
				sb.append( ( char ) readByte );
			}

			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch( java.io.FileNotFoundException e ) {
			stepContribution.setExitStatus(ExitStatus.FAILED);
			throw new FileException(e.getMessage(), BatchCode.FILE_NOT_FOUND);	 	

		} catch( java.io.IOException e ) {
			stepContribution.setExitStatus(ExitStatus.FAILED);
			throw new FileException(e.getMessage(), BatchCode.FILE_NOT_FOUND);	 

		} finally {
			if (fileHandler != null )
				try {
					fileHandler.close();
				} catch (IOException e) {
					logger.error("Exception closing \"fileHandler\" while validating FileHeader and File Footer. %s", e.getMessage());
				}
		}
	}
}
