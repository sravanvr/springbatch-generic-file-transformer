package io.github.batchservices.processor;

import java.math.BigDecimal;
import java.util.Map;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.Transactions;
import io.github.batchservices.domain.TransactionsAddenda;
import io.github.batchservices.domain.global.BankExportLog;
import io.github.batchservices.domain.global.BankLog;
import io.github.batchservices.domain.global.BatchFooter;
import io.github.batchservices.domain.global.BatchHeader;
import io.github.batchservices.domain.global.CustomTranCodeMappings;
import io.github.batchservices.domain.global.FileFooter;
import io.github.batchservices.domain.global.FileHeader;
import io.github.batchservices.exception.GenericFileTransformerException;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.mapper.GenericCustomFormatMapper;
import io.github.batchservices.service.FileLogService;
import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.SeverityLevel;
import io.github.batchservices.util.Utils;
import io.github.batchservices.writer.CustomFormatHeaderWriter;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractItemProcessor {
	
	@Autowired
	protected ExecutionContextAccessor executionContextAccessor;
	
	@Autowired
    CustomFormatHeaderWriter customFormatHeaderWriter;
	
	@Autowired
	GenericCustomFormatMapper nachaCustomFormatMapper;
	
	@Autowired
    FileLogService fileLogService;
	
	@Autowired
	BankLog bankLog;
	
	@Autowired
	BankExportLog bankExportLog;

	private static final XLogger logger = XLoggerFactory
			.getXLogger(AbstractItemProcessor.class);

	
	private BatchHeader prevBatchHeader = null;
	private Transactions prevTransactionsRecord = null;
	private EntityMarker prevRecord = null;
	
	Integer batchHeaderCount = 0;
	int recordCount = 0;
	
	public EntityMarker doProcess(EntityMarker entity) throws Exception {
		
		AbstractEntity abstractEntity = (AbstractEntity) entity;

		Integer fileLogId = executionContextAccessor.getFileLogId();
		Map<Integer, BankLog> bankLogMap = executionContextAccessor.getBankLogMap();		
		Map<Integer, CustomTranCodeMappings> transCodesMap = executionContextAccessor.getCustomTranCodeMap();
		
		Integer rowNum = abstractEntity.getRowNumber();		
		// Update ExecutionContext with current row number for any Fatal Exception logging.
		executionContextAccessor.setRowNum(rowNum);

		/*********************************************************************************************
		 * FILE HEADER:
		 **********************************************************************************************/
		if (abstractEntity instanceof FileHeader) {			
			prevRecord = (FileHeader) abstractEntity;			
		}

		/*********************************************************************************************
		 * BATCH HEADER:
		 **********************************************************************************************/
		else if (abstractEntity instanceof BatchHeader) {
			// If it's batch header that means the last set (one or more) of nacha transactions have already been mapped. 
			// When BatchHeader is reached, reset them to track new batch.  
			prevBatchHeader = null;
			prevTransactionsRecord = null;
			prevRecord = null;
			// Begin tracking new batch  		
			prevBatchHeader = (BatchHeader) abstractEntity; 
			prevRecord = (BatchHeader) abstractEntity;
			
			if (bankLogMap == null || ! bankLogMap.containsKey(Utils.getBankNumberFromGenericBatchHeader((BatchHeader) abstractEntity))){
				// Create Bank Log
				bankLog = fileLogService.createBanklog((BatchHeader) abstractEntity);
				
				// Create Bank Export Log
				bankExportLog = fileLogService.createBankExportLog((BatchHeader) abstractEntity, bankLog.getBankLogId());
				
				bankExportLog.setTotalDebit(BigDecimal.valueOf(0));
				bankExportLog.setTotalCredit(BigDecimal.valueOf(0));
			}
			++batchHeaderCount;
		}

		/*********************************************************************************************
		 * BATCH FOOTER:
		 **********************************************************************************************/
		else if (abstractEntity instanceof BatchFooter) {
			// Always Store current record in "prevRecord" so the following record, in case of "TransactionsAddenda" record, can verify 
			// if the previous record is Transactions. If that's not the case then we have to handle this as exception.
			prevRecord = (BatchFooter) abstractEntity;
		
			// Update Bank Log
			fileLogService.updateBanklog(bankLog);
			
			// Update Bank Export Log
			fileLogService.updateBankExportLog(bankExportLog);
		}

		/*********************************************************************************************
		 * FILE FOOTER:
		 **********************************************************************************************/
		else if (abstractEntity instanceof FileFooter) {
			if (! (prevRecord instanceof FileFooter)){
				fileLogService.updateFileLogStatusOnCompletion(fileLogId, batchHeaderCount);
			}
			prevRecord = (FileFooter) abstractEntity;
		} 
		else{
			if (abstractEntity instanceof Transactions){ 
				prevTransactionsRecord = (Transactions) abstractEntity;
				prevRecord = prevTransactionsRecord;				
				try{
					if (transCodesMap.get(prevTransactionsRecord.getTransactionCode()).getDbCR().equals("DB")){				
						bankExportLog.setTotalDebit(bankExportLog.getTotalDebit().add(BigDecimal.valueOf(prevTransactionsRecord.getTransactionAmount(),2)));
					}

					if (transCodesMap.get(prevTransactionsRecord.getTransactionCode()).getDbCR().equals("CR")){				
						bankExportLog.setTotalCredit(bankExportLog.getTotalCredit().add(BigDecimal.valueOf(prevTransactionsRecord.getTransactionAmount(),2)));
					}
				} catch (Exception e){
					String errorMesage = "Tran Code " + prevTransactionsRecord.getTransactionCode() + " is not found in tblCustomTranCodeMappings table";
					throw new GenericFileTransformerException(errorMesage, BatchCode.RUNTIME_EXCEPTION, SeverityLevel.FATAL);
				}					
			} 
			else if (abstractEntity instanceof TransactionsAddenda) {
				// Two legged transactions, hence increment detail record count for each Addenda record.
				bankExportLog.setRecordCnt(++ recordCount);
				
				// This is the only place we return a mapped record. If control did not enter here, we return null.
				TransactionsAddenda transactionsAddenda = (TransactionsAddenda) abstractEntity;				
				if (prevBatchHeader != null && prevTransactionsRecord != null && (AbstractEntity)prevRecord instanceof Transactions) {
					return nachaCustomFormatMapper.mapGenericToCustomFormat(transactionsAddenda, prevTransactionsRecord, prevBatchHeader);
				}
			}	
		}
		// Return null, had it been a TransactionsAddenda record, mapped custom record would have already been returned and control never gets here.
		return null;
	}
}
