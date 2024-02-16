package io.github.batchservices.writer;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.global.ErrorLog;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.repository.global.ErrorLogRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/***
 * 
 * @author rv250129
 *
 */
public abstract class AbstractItemWriter {
	
    @Autowired
    protected ExecutionContextAccessor executionContextAccessor;
    
//    @Autowired
//	private BatchCounts batchCounts;
    
    @Autowired
    private ErrorLogRepository errorLogRepository;
    
	public void prepareErrorLog(AbstractEntity entity, List<ErrorLog> errorLog){
		
		// Update ExecutionContext with current row number for any Fatal Exception logging
		executionContextAccessor.setRowNum(entity.getRowNumber());
		
        // Prepare errorLog list for the current entity
		if (!entity.getErrorLog().isEmpty()) {
			errorLog.addAll(entity.getErrorLog());
		}
		
//		// Find skipped count
//		if (entity.getRecordState() == SeverityLevel.ERROR && entity instanceof AbstractDetailRecord) {
//			batchCounts.incrementRejectedCount();
//		}
		
	}
	
	public void insertErrorLog(List<ErrorLog> errorLog) throws Exception{
//		if(!errorLog.isEmpty()) {
//        	try{
//        		errorLogRepository.insertBatch(errorLog);
//        		executionContextAccessor.setErrorLogInsertStatus(SUCCESS);
//        	}catch (Exception e){ 
//        		// In this case Spring JDBCTemplate will roll back inserted ErrorLog's of each entity of the current chunk.
//        		executionContextAccessor.setErrorLogInsertStatus(FAILED);
//        		throw (e);
//        	}
//        }
	}
}
