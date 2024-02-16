package io.github.batchservices.repository.global;

import io.github.batchservices.domain.global.ErrorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional(transactionManager = "transactionManager")
public class ErrorLogRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional (propagation=Propagation.REQUIRES_NEW)
	public int[] insertBatch(final List<ErrorLog> errorLogList) throws Exception{
		
		String sql = "INSERT INTO Error_Log " +
				" (file_log_id, bank_log_id, severity_level_id, err_message, record_num) " +
				" VALUES (?, ?, ?, ?, ?)" ;
		
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ErrorLog errorLog = errorLogList.get(i);
					ps.setInt(1, errorLog.getFileLogId());
					if(errorLog.getBankLogId() != null)
						ps.setInt(2, errorLog.getBankLogId());
					else
						ps.setInt(2, 0);
					ps.setInt(3, errorLog.getSeverityLevelId());
					ps.setString(4, errorLog.getErrMessage());
					ps.setInt(5, errorLog.getRecordNum());					
				}
				
				@Override
				public int getBatchSize() {
					return errorLogList.size();
				}
			
			});					
	
	}

	// Use this to insert single errorLog record rather than a batch.
	@Transactional (propagation=Propagation.REQUIRES_NEW)
	public int insertErrorLog(ErrorLog errorLog) {
		
		String sql = "INSERT INTO Error_Log " +
				" (file_log_id, bank_log_id, severity_level_id, err_message, record_num) " +
				" VALUES (?, ?, ?, ?, ?)" ;
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					
					ps.setInt(1, errorLog.getFileLogId());
					if(errorLog.getBankLogId() != null)
						ps.setInt(2, errorLog.getBankLogId());
					else
						ps.setInt(2, 0);
					ps.setInt(3, errorLog.getSeverityLevelId());
					ps.setString(4, errorLog.getErrMessage());
					ps.setInt(5, errorLog.getRecordNum());
				}
				
			});					
	
	}

	@Transactional(readOnly = true)
	public ErrorLog findErrorLogByKeys(int fileLogId, int bankLogId) {
		return jdbcTemplate.queryForObject("SELECT " +
				"  error_log_id, file_log_id, bank_log_id, " +
				"  severity_level_id, err_message, record_num " +
				" FROM error_log WHERE file_log_id = ? and bank_log_id = ?", 
				  new Object[] { fileLogId, bankLogId }, new ErrorLogRowMapper());
	}
	
	public void deleteForSingleBatch (int fileLogId, int bankLogId) {
		jdbcTemplate.update("DELETE FROM error_log WHERE file_log_id = ? AND bank_log_id = ?",
				new Object[] { fileLogId, bankLogId });
	}
	
	public void deletebyFileLogId (int fileLogId) {
		jdbcTemplate.update("DELETE FROM error_log WHERE file_log_id = ?",
				new Object[] { fileLogId });
	}

}

class ErrorLogRowMapper implements RowMapper<ErrorLog> {

	@Override
	public ErrorLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ErrorLog errorLog = new ErrorLog();
		
		errorLog.setErrorLogId(rs.getInt("error_log_id"));
		errorLog.setFileLogId(rs.getInt("file_log_id"));
		errorLog.setBankLogId(rs.getInt("bank_log_id"));
		errorLog.setSeverityLevelId(rs.getInt("severity_level_id"));
		errorLog.setErrMessage(rs.getString("err_message"));
		errorLog.setRecordNum(rs.getInt("record_num"));
		
		return errorLog;
	
	}
	
}
	

