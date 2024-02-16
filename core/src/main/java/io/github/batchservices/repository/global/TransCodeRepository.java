package io.github.batchservices.repository.global;

import io.github.batchservices.domain.global.CustomTranCodeMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * 
 * @author rv250129
 *
 */
@Repository
public class TransCodeRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public CustomTranCodeMappings getTransCodes() {
		return jdbcTemplate.queryForObject("SELECT " +
				"  ExportFileTranCode, CustomTranCode, ModuleID" +
				" FROM tblCustomTranCodeMappings", 
				  new TransCodeRowMapper());
	}
}

class TransCodeRowMapper implements RowMapper<CustomTranCodeMappings> {
	@Override
	public CustomTranCodeMappings mapRow(ResultSet rs, int rowNum) throws SQLException {		
		CustomTranCodeMappings transCode = new CustomTranCodeMappings();		
		transCode.setExportFileTranCode(rs.getInt("ExportFileTranCode"));
		transCode.setCustomTranCode(rs.getInt("CustomTranCode"));
		transCode.setModuleID(rs.getInt("ModuleID"));		
		return transCode;	
	}	
}
	

