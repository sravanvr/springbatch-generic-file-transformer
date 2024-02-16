package io.github.batchservices.repository.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.batchservices.domain.global.CustomTranCodeMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/***
 * 
 * @author rv250129
 *
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class CustomTranCodeMappingsRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public Map<Integer, CustomTranCodeMappings> getTransCodes() {

		String sql = "select * from nHome.dbo.tblCustomTranCodeMappings";  			

		List<CustomTranCodeMappings> customTranCodeMappingsList = new ArrayList<CustomTranCodeMappings>();

		try {
			customTranCodeMappingsList = jdbcTemplate.query(sql,
					new CustomTranCodeMappingsRowMapper());		

		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		Map<Integer, CustomTranCodeMappings> map = customTranCodeMappingsList.stream()
			      .collect(Collectors.toMap(
			    		  CustomTranCodeMappings::getExportFileTranCode, 
			    		  customTranCodeMappings -> customTranCodeMappings)
			    		  );		
		return map;
	}
}

class CustomTranCodeMappingsRowMapper implements RowMapper<CustomTranCodeMappings> {
	public CustomTranCodeMappings mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomTranCodeMappings customTranCodeMappings = new CustomTranCodeMappings();
		customTranCodeMappings.setExportFileTranCode(rs.getInt("ExportFileTranCode"));
		customTranCodeMappings.setCustomTranCode(rs.getInt("CustomTranCode"));
		customTranCodeMappings.setModuleID(rs.getInt("ModuleID"));
		customTranCodeMappings.setDbCR(rs.getString("DB_CR"));
		return customTranCodeMappings;
	}
}

