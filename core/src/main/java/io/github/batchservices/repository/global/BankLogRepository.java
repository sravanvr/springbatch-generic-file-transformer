package io.github.batchservices.repository.global;

import io.github.batchservices.domain.global.BankLog;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.util.ProcessStatus;
import io.github.batchservices.util.SeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;


@Repository
public class BankLogRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    ExecutionContextAccessor executionContextAccessor;
	
	@Transactional
	public BankLog create(BankLog bankLog) {
		
		KeyHolder holder = new GeneratedKeyHolder();
			
		try{
		jdbcTemplate.update(new PreparedStatementCreator() {
				
			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
			        throws SQLException {

				final String BANKLOG_INSERT_SQL = 
						" INSERT INTO bank_log (" +
						"   file_log_id, bank_domain_name, process_status_id, " +
						"   create_date, ProcessingStartDate, effective_date, BatchSequenceTxt," +
						"   ReprocessType, AsOfDate )" +
						" VALUES (?, ?, ?, getdate(), getdate(), ?, ?, ?, ?)" ;

				PreparedStatement ps = connection.prepareStatement(BANKLOG_INSERT_SQL, 
			    									Statement.RETURN_GENERATED_KEYS);
			    ps.setInt(1, bankLog.getFileLogId());
			    ps.setString(2, bankLog.getBankDomainName());
			    ps.setInt(3, bankLog.getProcessStatusId());
			    
			    ps.setTimestamp(4, new Timestamp(bankLog.getEffectiveDate().getTime()));
			    //ps.setTimestamp(4, (new Timestamp((new Date()).getTime())));
			    
			    ps.setString(5, bankLog.getBatchSequenceTxt());
			    ps.setInt(6, bankLog.getReprocessType());
				
			    ps.setTimestamp(7, new Timestamp(bankLog.getAsOfDate().getTime()));
			    //ps.setTimestamp(7, (new Timestamp((new Date()).getTime())));
			    
			    return ps;
			}
		}, holder);
		
		}
		catch (Exception e){
			System.out.println ("Exception: " + e.getMessage());
		}
			
		int newBankLogId = holder.getKey().intValue();
		bankLog.setBankLogId(newBankLogId);
		return findBankLogByBankLogId(newBankLogId);
	}
	
	@Transactional (propagation=Propagation.REQUIRES_NEW)
	public int updateOnCompletion(Integer fileLogId, Integer bankLogId, 
									Integer processStatusId)  {
		String updateSql = 
				" UPDATE bank_log SET process_status_id = ?, complete_date = getdate() "
			  + " WHERE file_log_id = ? and bank_log_id = ?" ;
		
		int rows = jdbcTemplate.update(updateSql,
						new Object[]{processStatusId, fileLogId, bankLogId});

        // Update Bank Log in execution context
        BankLog bankLog = executionContextAccessor.getBankLogMap().get(executionContextAccessor.getBankNumber());
        if(bankLog != null)
            bankLog.setProcessStatusId(processStatusId);
		return rows;
	}

	public int updateOnCompletion(Integer fileLogId, Integer bankLogId)  {

		Integer processStatusId = ProcessStatus.SUCCESSFUL.getValue();
		// Check if there are any errors in Error_Log table for the given File Log and Bank Log ID.
		String selectSql = "SELECT COUNT(1) FROM error_log WHERE file_log_id = ? AND bank_log_id = ? AND severity_level_id = ?";
		Integer count = jdbcTemplate.queryForObject(selectSql, new Object[] {fileLogId, bankLogId, SeverityLevel.ERROR.getValue()}, Integer.class);

		if(count > 0)
			processStatusId = ProcessStatus.WARNINGS.getValue();

		return updateOnCompletion(fileLogId, bankLogId, processStatusId);
	}
	
	@Transactional(readOnly = true)
	public BankLog findBankLogByBankLogId(int bankLogId) {
		return jdbcTemplate.queryForObject("SELECT file_log_id, bank_log_id,"
				+ " bank_domain_name, process_status_id, create_date, effective_date, "
				+ " ProcessingStartDate, complete_date, BatchSequenceTxt, ReprocessType, "
				+ " AsOfDate "
				+ " FROM bank_log WHERE bank_log_id = ?", new Object[] { bankLogId }, new BankLogRowMapper());
	}
	
	@Transactional(readOnly = true)
	public Timestamp getMaxEffectiveDateForBank (String bankDomainName, int dataTypeId) {
		
		Timestamp dt = jdbcTemplate.queryForObject("SELECT MAX(b.effective_date) FROM bank_log b, file_log f "
				+ " WHERE b.bank_domain_name = ? AND"
				+ " b.file_log_id = f.file_log_id AND" 
				+ " f.file_type_id = 1 AND"
				+ " f.data_type_id = ? AND"
				+ " b.process_status_id IN (4,6)", new Object[] {bankDomainName, dataTypeId}, Timestamp.class);
		
		return dt;		
	}
	
	@Transactional(readOnly = true)
	public Boolean foundNewerAsOfDateForBank (String bankDomainName, Integer dataTypeId, Timestamp asOfDate) {
		
		Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM bank_log b, file_log f "
				+ " WHERE 	b.bank_domain_name = ? AND"
				+ " b.file_log_id = f.file_log_id AND" 
				+ " f.file_type_id = 1 AND"
				+ " f.data_type_id = ? AND"
				+ " b.process_status_id IN (4,6) AND"
				+ " b.AsOfDate > ?", new Object[] {bankDomainName, dataTypeId, asOfDate}, Integer.class);
		
		return (count > 0 ? true : false);	
	}

	@Transactional(readOnly = true)
	public List<BankLog> findBankLogByBankDomainNameAndFileLogId (String bankDomainName, Integer fileLogId) {
		
		return jdbcTemplate.query("SELECT file_log_id, bank_log_id,"
				+ " bank_domain_name, process_status_id, create_date, effective_date, "
				+ " ProcessingStartDate, complete_date, BatchSequenceTxt, ReprocessType, "
				+ " AsOfDate "
				+ " FROM bank_log WHERE file_log_id = ? and bank_domain_name = ?", new Object[] { fileLogId, bankDomainName  }, new BankLogRowMapper()); 
				
	}
	
	public void deleteForSingleBatch (int fileLogId, int bankLogId) {
		jdbcTemplate.update("DELETE FROM bank_log WHERE file_log_id = ? AND bank_log_id = ?",
				new Object[] { fileLogId, bankLogId });
	}
	
	public void deletebyFileLogId (int fileLogId) {
		jdbcTemplate.update("DELETE FROM bank_log WHERE file_log_id = ?",
				new Object[] { fileLogId });
	}
}


class BankLogRowMapper implements RowMapper<BankLog> {

	@Override
	public BankLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BankLog bankLog = new BankLog();
		
		bankLog.setFileLogId(rs.getInt("file_log_id"));
		bankLog.setBankLogId(rs.getInt("bank_log_id"));
		bankLog.setBankDomainName(rs.getString("bank_domain_name"));
		bankLog.setProcessStatusId(rs.getInt("process_status_id"));
		bankLog.setCreateDate(rs.getTimestamp("create_date"));
		bankLog.setProcessingStartDate(rs.getTimestamp("ProcessingStartDate"));
		bankLog.setCompleteDate(rs.getTimestamp("complete_date"));
		bankLog.setBatchSequenceTxt(rs.getString("BatchSequenceTxt"));
		bankLog.setAsOfDate(rs.getTimestamp("AsOfDate"));
		
		return bankLog;
	
	}
}
	

