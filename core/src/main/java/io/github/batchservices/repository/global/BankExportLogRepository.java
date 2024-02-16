package io.github.batchservices.repository.global;

import io.github.batchservices.domain.global.BankExportLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
@Transactional(transactionManager = "transactionManager")
public class BankExportLogRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public BankExportLog create(BankExportLog bankExportLog) {
		

		jdbcTemplate.update(new PreparedStatementCreator() {
				
		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
			        throws SQLException {

				final String BANKIMPORTLOG_INSERT_SQL = 
						" INSERT INTO bank_export_log (" +
						"   file_log_id, bank_log_id, record_cnt, create_date, " +
						"   total_credit, start_date, total_debit) " +
						" VALUES (?, ?, ?, ?, ?, ?, ?)" ;

				PreparedStatement ps = connection.prepareStatement(BANKIMPORTLOG_INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				
			    ps.setInt(1, bankExportLog.getFileLogId());
                ps.setInt(2, bankExportLog.getBankLogId());
			    ps.setInt(3, bankExportLog.getRecordCnt());
			    ps.setTimestamp(4, new Timestamp(bankExportLog.getCreateDate().getTime()));
			    ps.setBigDecimal(5, bankExportLog.getTotalCredit());
			    ps.setTimestamp(6, new Timestamp(bankExportLog.getStartDate().getTime()));
			    ps.setBigDecimal(7, bankExportLog.getTotalDebit());
			    return ps;
			}
		});

		return findBankExportLogByKeys(bankExportLog.getFileLogId(), bankExportLog.getBankLogId());
	}

    public int updateOnCompletion(BankExportLog bankExportLog)  {

        String updateSql =
                " UPDATE bank_export_log SET record_cnt = ?, total_credit = ?, total_debit = ? "
                        + " WHERE file_log_id = ? and bank_log_id = ?" ;

        int rows = jdbcTemplate.update(updateSql,
                new Object[]{bankExportLog.getRecordCnt(), bankExportLog.getTotalCredit(), bankExportLog.getTotalDebit(),
        		bankExportLog.getFileLogId(), bankExportLog.getBankLogId()});
        return rows;
    }

    @Transactional(readOnly = true)
	public BankExportLog findBankExportLogByKeys(int fileLogId, int bankLogId) {
  
		return jdbcTemplate.queryForObject("SELECT " +
				"   file_log_id, bank_log_id, record_cnt, create_date, " +
				"   total_credit, start_date, total_debit " +
				" FROM bank_export_log WHERE file_log_id = ? AND bank_log_id = ?",
				  new Object[] { fileLogId, bankLogId }, new BankExportLogRowMapper());
	}
    
//	public void deleteForSingleBatch (int fileLogId, int bankLogId) {
//		jdbcTemplate.update("DELETE FROM bank_import_log WHERE file_log_id = ? AND bank_log_id = ?",
//				new Object[] { fileLogId, bankLogId });
//	}
//	
//	public void deletebyFileLogId (int fileLogId) {
//		jdbcTemplate.update("DELETE FROM bank_import_log WHERE file_log_id = ?",
//				new Object[] { fileLogId });
//	}
	
}
	
class BankExportLogRowMapper implements RowMapper<BankExportLog> {

	@Override
	public BankExportLog mapRow(ResultSet rs, int rowNum) throws SQLException {

		BankExportLog bankExportLog = new BankExportLog();

		bankExportLog.setFileLogId(rs.getInt("file_log_id"));
		bankExportLog.setBankLogId(rs.getInt("bank_log_id"));
		bankExportLog.setRecordCnt(rs.getInt("record_cnt"));
		bankExportLog.setCreateDate(rs.getDate("create_date"));
		bankExportLog.setTotalDebit(rs.getBigDecimal("total_debit"));
		bankExportLog.setTotalCredit(rs.getBigDecimal("total_credit"));
		bankExportLog.setStartDate(rs.getDate("start_date"));			
		return bankExportLog;		
	}
}
	
