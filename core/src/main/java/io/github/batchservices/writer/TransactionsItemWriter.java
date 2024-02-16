package io.github.batchservices.writer;

import io.github.batchservices.aop.TrackTime;
import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.repository.global.ErrorLogRepository;
import io.github.batchservices.service.FileLogService;

import io.github.batchservices.util.Constants;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class TransactionsItemWriter extends AbstractItemWriter implements ItemWriter<EntityMarker>, ItemWriteListener<EntityMarker> {

	private static final XLogger logger = XLoggerFactory.getXLogger(TransactionsItemWriter.class);

//	@Autowired
//	TransactionsRepository transactionsRepository;

	@Autowired
	private ErrorLogRepository errorLogRepository;

	@Autowired
	private FileLogService fileLogService;
    
//    @Autowired
//	private BatchCounts batchCounts;
    
	@Override
	public void beforeWrite(List<? extends EntityMarker> list) {
		executionContextAccessor.setErrorLogInsertStatus(Constants.FAILED); // Set to FAILED
	}

	@Override
	public void afterWrite(List<? extends EntityMarker> list) {
		executionContextAccessor.setErrorLogInsertStatus(Constants.FAILED); // Reset to FAILED after write is complete.
	}

	@Override
	public void onWriteError(Exception e, List<? extends EntityMarker> list) {
		//importLoggingService.handleOnErrorEvent("AccountItemWriter", e, list);
	}

	@Override
	@TrackTime
	public void write(List<? extends EntityMarker> list) throws Exception {

//		List<Transactions> Transactions = new ArrayList<>();
//		List<ErrorLog> errorLog = new ArrayList<>();
//
//		for (EntityMarker entity : list) {
//
//			AbstractEntity abstractEntity = (AbstractEntity) entity;
//			prepareErrorLog(abstractEntity, errorLog);
//			
//			if(abstractEntity instanceof Transactions){
//				Transactions transaction =  (Transactions) abstractEntity;
//
//				// Whenever the detail record is skipped...
//				if (transaction.getRecordState() == SeverityLevel.ERROR){
//					if (transaction.getTransCode().equals("01") || transaction.getTransCode().equals("1"))
//						batchCounts.addToTotalCreditRejected(transaction.getTransAmount());
//					if (transaction.getTransCode().equals("02") || transaction.getTransCode().equals("2"))
//						batchCounts.addToTotalDebitRejected(transaction.getTransAmount());
//				} else {
//					if (transaction.getTransCode().equals("01") || transaction.getTransCode().equals("1"))
//						batchCounts.addToTotalCreditAdded(transaction.getTransAmount());
//					if (transaction.getTransCode().equals("02") || transaction.getTransCode().equals("2"))
//						batchCounts.addToTotalDebitAdded(transaction.getTransAmount());
//	
//					transaction.setFileName(executionContextAccessor.getFileNameWithOutExtension());
//					transaction.setFileLogId(executionContextAccessor.getFileLogId());					
//					Transactions.add((Transactions)entity);						
//				}
//
//			}
//			
//		}
//
//		insertErrorLog(errorLog);
//
//		if (!Transactions.isEmpty()) {
//			if (executionContextAccessor.getFileSequence().equals("0") || executionContextAccessor.getFileSequence().equals("Z")) {
//				int ret[] = transactionsRepository.insertPostedTransactions(Transactions);
//				batchCounts.incrementAddedCount(Arrays.stream(ret).sum());
//			}
//			else {
//				int []ret = transactionsRepository.insertMemoPostTransactions(Transactions);
//				batchCounts.incrementAddedCount(Arrays.stream(ret).sum());
//			}			
//		}

	}
}
