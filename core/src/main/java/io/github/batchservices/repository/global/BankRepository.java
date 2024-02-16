package io.github.batchservices.repository.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.batchservices.domain.global.Bank;
import io.github.batchservices.exception.ApplicationException;
import io.github.batchservices.util.BatchCode;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
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
public class BankRepository {

	private static final XLogger logger = XLoggerFactory.getXLogger(BankRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Retrieves configuration of all Banks associated with the Bank Run Stream ID
	 * @param bankId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Bank findByBankNumber(Integer batchHeaderCompanyIdentification) {
		String sql = "select BankId, BankDomainName, BankNo, CheckDigit, CCFIID from Bank where BankNo = ?";
		List<Bank> banks = new ArrayList<Bank>();
		try {
			banks = jdbcTemplate.query(sql, new Object[] { batchHeaderCompanyIdentification },
					new BankRowMapper());
		} catch (DataAccessException e) {
			throw (e);
		}
		if (banks.isEmpty()) {
			throw new ApplicationException("Bank Number "+ batchHeaderCompanyIdentification +" is not configured.", BatchCode.APPLICATION_EXCEPTION);
		}
		return banks.get(0);
	}
	
	public void updateBankLastImport(Integer bankNo) {
		jdbcTemplate.update("UPDATE Bank SET LastImport = getdate() where BankNo = ?",
				new Object[] { bankNo });
	}

	@Transactional(readOnly = true)
	public String findCCFIID(Integer batchHeaderCompanyIdentification)  {		
		String selectSql = "select CCFIID from Bank where BankNo = ?";
		String ccfid = jdbcTemplate.queryForObject(selectSql, new Object[] {batchHeaderCompanyIdentification}, String.class);
		return ccfid;
	}
}

class BankRowMapper implements RowMapper<Bank> {

	public Bank mapRow(ResultSet rs, int rowNum) throws SQLException {

		Bank bank = new Bank();
		bank.setBankID(rs.getString("BankID"));
		bank.setBankDomainName(rs.getString("BankDomainName"));
		bank.setBankNo(rs.getInt("BankNo"));
		bank.setCheckDigit(rs.getInt("CheckDigit"));
		bank.setCcfid(rs.getInt("CCFIID"));
		return bank;
	}
}

