package io.github.batchservices.repository.global;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.batchservices.domain.global.FileLog;
import org.springframework.jdbc.core.RowMapper;

public class FileLogMapper implements RowMapper<FileLog>{
		
	public FileLog mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			FileLog fileLog = new FileLog();
			
			fileLog.setFileLogId(rs.getInt("file_log_id"));
			fileLog.setFileTypeId(rs.getInt("file_type_id"));
			fileLog.setDataTypeId(rs.getInt("data_type_id"));
			fileLog.setProcessStatusId(rs.getInt("process_status_id"));
			fileLog.setFileName(rs.getString("file_name"));
			fileLog.setFileSize(rs.getLong("file_size"));
			fileLog.setFileCreateDate(rs.getTimestamp("file_create_date"));
			fileLog.setFileLastWriteDate(rs.getTimestamp("file_last_write_date"));
			fileLog.setFileLastAccessDate(rs.getTimestamp("file_last_access_date"));
			fileLog.setCreateDate(rs.getTimestamp("create_date"));
			fileLog.setProcessingStartDate(rs.getTimestamp("processingStartDate"));	
			fileLog.setProcessingCompleteDate(rs.getTimestamp("processingCompleteDate"));	
			fileLog.setRunstreamId(rs.getString("runstreamId"));
			fileLog.setFileSequenceTxt(rs.getString("fileSequenceTxt"));
			fileLog.setPriority(rs.getInt("priority"));
			fileLog.setFilePostDate(rs.getDate("filePostDate"));
			fileLog.setBatchCountNbr(rs.getInt("batchCountNbr"));
			fileLog.setReprocessType(rs.getInt("reprocessType"));
			fileLog.setServerName(rs.getString("serverName"));
			
			return fileLog;
		
		}
}
