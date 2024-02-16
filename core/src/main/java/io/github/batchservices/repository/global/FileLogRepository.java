package io.github.batchservices.repository.global;

import io.github.batchservices.util.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.batchservices.domain.global.FileLog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


@Repository
public class FileLogRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional (propagation=Propagation.REQUIRES_NEW)
	public FileLog create(FileLog fileLog) {
		
		KeyHolder holder = new GeneratedKeyHolder();
			
		jdbcTemplate.update(new PreparedStatementCreator() {
				
			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
			        throws SQLException {
				
				Boolean bProcessingComplete = false;
				final String FILELOG_INSERT_SQL ;
				
				if (fileLog.getProcessStatusId() > ProcessStatus.PROCESSING.getValue()) {
					 FILELOG_INSERT_SQL = 
							" INSERT INTO file_log (" +
							"   file_type_id, data_type_id, process_status_id, file_name," +
							"   file_size, file_create_date, file_last_write_date, file_last_Access_date," +
							"   create_date, processingStartDate, runstreamId, fileSequenceTxt," +
							"   priority, filePostDate, reprocessType, serverName, ProcessingCompleteDate )" +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, getdate(), getdate(), ?, ?, ?, ?, ?, ?, getdate())" ;
				} else {
					 FILELOG_INSERT_SQL = 
							" INSERT INTO file_log (" +
							"   file_type_id, data_type_id, process_status_id, file_name," +
							"   file_size, file_create_date, file_last_write_date, file_last_Access_date," +
							"   create_date, processingStartDate, runstreamId, fileSequenceTxt," +
							"   priority, filePostDate, reprocessType, serverName )" +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, getdate(), getdate(), ?, ?, ?, ?, ?, ?)" ;

				}

				PreparedStatement ps = connection.prepareStatement(FILELOG_INSERT_SQL, 
			    									Statement.RETURN_GENERATED_KEYS);
			    ps.setInt(1, fileLog.getFileTypeId());
			    ps.setInt(2, fileLog.getDataTypeId());
			    ps.setInt(3, fileLog.getProcessStatusId());
			    ps.setString(4, fileLog.getFileName());
			    ps.setLong(5, fileLog.getFileSize());
			    ps.setTimestamp(6, fileLog.getFileCreateDate());
			    ps.setTimestamp(7, fileLog.getFileLastWriteDate());
			    ps.setTimestamp(8, fileLog.getFileLastAccessDate());            
			    ps.setString(9, fileLog.getRunstreamId());
			    ps.setString(10, fileLog.getFileSequenceTxt());
			    ps.setInt(11, fileLog.getPriority());
			    //ps.setNull(11, java.sql.Types.INTEGER);
			    ps.setDate(12, fileLog.getFilePostDate());
			    ps.setInt(13, fileLog.getReprocessType());
			    ps.setString(14, fileLog.getServerName());

			    return ps;
			}
		}, holder);
			
		int newFileLogId = holder.getKey().intValue();
		fileLog.setFileLogId(newFileLogId);

		return fileLog;
	}
	
	@Transactional
	public int updatePriorityAndStatus(Integer fileLogId, Integer priority, 
							Integer processStatusId)  {
		String updateSql = 
				" UPDATE file_log SET priority = ?, process_status_id = ? WHERE file_log_id = ?" ;
		
		int rows = jdbcTemplate.update(updateSql, 
								new Object[]{priority, processStatusId, fileLogId});
		return rows;		
	}

	@Transactional
	public int updatePriorityAndStatus(Integer fileLogId, Integer priority, 
			Integer processStatusId, String serverName)  {
		
		String updateSql = "";
		if (processStatusId < ProcessStatus.SUCCESSFUL.getValue() || processStatusId == ProcessStatus.LOCKED.getValue()) {
			updateSql = 
					"UPDATE file_log SET priority = ?, process_status_id = ?, ServerName = ?, ProcessingStartDate = getdate()"
							+ " WHERE file_log_id = ?" ;
		} else {
			updateSql = 
					"UPDATE file_log SET priority = ?, process_status_id = ?, ServerName = ?"
							+ " WHERE file_log_id = ?" ;
		}
		
		int rows = jdbcTemplate.update(updateSql, 
						new Object[]{priority, processStatusId, serverName, fileLogId});
		return rows;		
	}
	
	@Transactional
	public int updateStatus(Integer fileLogId, Integer processStatusId)  {
		int rows = 0;
		
		if (processStatusId > ProcessStatus.PROCESSING.getValue()) {
			rows = updateOnCompletion(fileLogId, processStatusId);
		} else {
			String updateSql = 
			" UPDATE file_log SET process_status_id = ? WHERE file_log_id = ?" ;
		
			rows = jdbcTemplate.update(updateSql, 
							new Object[]{processStatusId, fileLogId});
		}
		return rows;		
	}
	
	@Transactional
	public int updateFileHeaderInfoAndStatus(FileLog fileLog, Integer fileLogId, Integer processStatusId)  {
		int rows = 0;
		Date filePostDate = fileLog.getFilePostDate();

		String updateSql = 
		  "UPDATE file_log SET filePostDate = ?, process_status_id = ? WHERE file_log_id = ?" ;
	
		rows = jdbcTemplate.update(updateSql, 
						new Object[]{filePostDate, processStatusId, fileLogId});

		return rows;		
	}
	
	@Transactional (propagation=Propagation.REQUIRES_NEW)
	public int updateOnCompletion(Integer fileLogId, Integer processStatusId)  {
	String updateSql = 
				" UPDATE file_log SET process_status_id = ? , ProcessingCompleteDate = getdate() "
			  + " WHERE file_log_id = ?" ;
		
		int rows = jdbcTemplate.update(updateSql, 
						new Object[]{processStatusId, fileLogId});
		return rows;		
	}	

	@Transactional
	public int updateBatchCountAndStatusOnCompletion(Integer fileLogId, String filePath, Integer processStatusId, Integer bachCount)  {
	String updateSql = 
				" UPDATE file_log SET file_name = ?, process_status_id = ? , BatchCountNbr = ?, ProcessingCompleteDate = getdate() "
			  + " WHERE file_log_id = ?" ;
		
		int rows = jdbcTemplate.update(updateSql, 
						new Object[]{filePath, processStatusId, bachCount, fileLogId});
		return rows;		
	}
	
	@Transactional
	public int updateOnCompletion(Integer fileLogId, Integer processStatusId, 
				String serverName)  {
		String updateSql = 
				" UPDATE file_log SET process_status_id = ? , ProcessingCompleteDate = getdate(),"
			  + " serverName  = ?"
			  + " WHERE file_log_id = ?" ;
		
		int rows = jdbcTemplate.update(updateSql, 
						new Object[]{processStatusId, serverName, fileLogId});
		return rows;		
	}	
	
	@Transactional
	public void deletebyFileLogId (int fileLogId) {
		jdbcTemplate.update("DELETE FROM file_log WHERE file_log_id = ?",
				new Object[] { fileLogId });
	}

	@Transactional(readOnly = true)
	public FileLog findFileLogById(int id) {
		return jdbcTemplate.queryForObject("SELECT file_log_id, file_type_id,"
				+ " data_type_id, process_status_id, file_name, file_size,"
				+ " file_create_date, file_last_write_date, file_last_access_date,"
				+ " create_date, processingStartDate, processingCompleteDate,"
				+ " runstreamId, fileSequenceTxt, priority, filePostDate, batchCountNbr, "
				+ " reprocessType, serverName"
				+ " FROM file_log WHERE file_log_id = ?", new Object[] { id }, new FileLogMapper());
	}

	@Transactional(readOnly = true)
	public FileLog findFileLogByShortFileName(String shortFilename) {
		return jdbcTemplate.queryForObject("SELECT file_log_id, file_type_id,"
				+ " data_type_id, process_status_id, file_name, file_size,"
				+ " file_create_date, file_last_write_date, file_last_access_date,"
				+ " create_date, processingStartDate, processingCompleteDate,"
				+ " runstreamId, fileSequenceTxt, priority, filePostDate, batchCountNbr, "
				+ " reprocessType, serverName"
				+ " FROM file_log WHERE file_name LIKE '%?'" , new Object[] { shortFilename }, new FileLogMapper());
	}


	@Transactional(readOnly = true)
	public Boolean anyLowerSequenceFilesPending (String runstreamId, 
					int dataTypeId, Date postingDate, String fileSequenceTxt, Integer fileLogId) {
				
		boolean result = false;		
		String query = " SELECT count(0) FROM file_log" +
						" WHERE runstreamid = ? AND file_type_id = 1 AND data_type_id = ?"  +
						" AND CONVERT(DATETIME, FilePostDate) <= ?" +
						" AND process_status_id IN (0, 7)" +
						" AND fileSequenceTxt < ? AND file_log_id <> ?";
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{runstreamId, dataTypeId, postingDate, fileSequenceTxt, fileLogId}, 
								Integer.class);
								
		if (count > 0) {
			result = true;
		}
		
		return result;
	}
		
	
	@Transactional(readOnly = true)
	public boolean foundDuplicateFile(String shortFilename, Integer fileLogId) {

		boolean result = false;		
		String query = " SELECT count(0) FROM file_log" +
						" WHERE file_name LIKE ? and (process_status_id < 7 OR process_status_id = 10) AND file_log_id <> ?";
		
		shortFilename = '%' + shortFilename + '%';
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{ shortFilename, fileLogId }, Integer.class);
		
		if (count > 0) {
			result = true;
		}
		return result;
	}	
	
	@Transactional(readOnly = true)
	public boolean foundDuplicateFile(String shortFilename, Integer dataTypeId, Integer fileLogId) {

		boolean result = false;
		String query = "";
		
		if (dataTypeId == 3) {
			query = " SELECT count(0) FROM file_log " 
						+ " WHERE file_name LIKE ? and (process_status_id <= 7 OR process_status_id = 10) "
						+ " AND file_log_id <> ?" ; 
		} else {
			query = " SELECT count(0) FROM file_log " 
					+ " WHERE file_name LIKE ? and (process_status_id < 7 OR process_status_id = 10) "
					+ " AND file_log_id <> ?";
		}
		
		shortFilename = '%' + shortFilename + '%';
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{shortFilename, fileLogId}, Integer.class);
		
		if (count > 0) {
			result = true;
		}
		return result;
	}	

	@Transactional(readOnly = true)
	public boolean foundSimilarFileProcessing (String runstreamId, 
					 				int dataTypeId, Integer fileLogId) {
		
		boolean result = false;
		String query = " SELECT count(0) FROM file_log" +
						" WHERE runstreamid  = ? AND file_type_id = 1 AND data_type_id = ?"  +
						" AND process_status_id in (1, 2, 3, 10) AND file_log_id <> ?";
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{runstreamId, dataTypeId, fileLogId}, Integer.class);
								
		if (count > 0) {
			result = true;
		}
		
		return result;
	}

	@Transactional(readOnly = true)
	public Date getPreviousFilePostDateForRS (String runstreamId, 
					 						Date filePostDate) {
		
		String query = " SELECT max(FilePostDate)" +
						" from file_log" +
						" WHERE file_type_id = 1 AND runstreamid = ?" +
						" AND FilePostDate < ?" ;
		
		Date dtVal = jdbcTemplate.queryForObject(query,
							new Object[]{runstreamId, filePostDate}, Date.class);
										
		return dtVal;
	}	

	@Transactional(readOnly = true)
	public Boolean checkFinalImportCompleteForPostedDate (String runstreamId,
				Integer dataTypeId, Date filePostDate) {
		
		String query = " SELECT count(0) FROM file_log" +
						" WHERE runstreamid  = ? AND file_type_id = 1 AND data_type_id = ?"  +
						" AND CONVERT(DATETIME, FilePostDate) = ?" +
						" AND process_status_id IN (4, 6, 11) AND fileSequenceTxt IN ('Z', '0')";
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{runstreamId, dataTypeId, filePostDate}, 
								Integer.class);	
		
		if (count > 0) 
			return true;
		else
			return false;
	
	}

	@Transactional(readOnly = true)
	public Boolean hasNewerFileProcessed (String runstreamId,
				Integer dataTypeId, Date filePostDate) {
		
		String query = " SELECT count(0) FROM file_log" +
						" WHERE runstreamid  = ? AND file_type_id = 1 AND data_type_id = ?"  +
						" AND CONVERT(DATETIME, FilePostDate) > ?" +
						" AND process_status_id IN (4, 6)";
		
		int count = jdbcTemplate.queryForObject(query,
							new Object[]{runstreamId, dataTypeId, filePostDate}, 
								Integer.class);	
		
		if (count > 0) 
			return true;
		else
			return false;
	
	}

}
