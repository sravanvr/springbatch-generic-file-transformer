package io.github.batchservices.domain.global;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class BankExportLog {
	
	private Integer fileLogId;
	private Integer bankLogId;
	private Integer recordCnt;
	private Date createDate;
	private BigDecimal totalDebit = new BigDecimal(0);
	private BigDecimal totalCredit = new BigDecimal(0);
	private Date startDate;
	
	public BankExportLog() {
		super();
	}

	public BankExportLog(Integer fileLogId, Integer bankLogId, Integer recordCnt, Date createDate, BigDecimal totalDebit, 
			BigDecimal totalCredit, Date startDate) {
		super();
		this.fileLogId = fileLogId;
		this.bankLogId = bankLogId;
		this.recordCnt = recordCnt;
		this.createDate = createDate;	
		this.totalDebit = totalDebit;
		this.totalCredit = totalCredit;
		this.startDate = startDate;
	}

	public Integer getFileLogId() {
		return fileLogId;
	}

	public BankExportLog setFileLogId(Integer fileLogId) {
		this.fileLogId = fileLogId;
		return this;
	}

	public Integer getBankLogId() {
		return bankLogId;
	}

	public BankExportLog setBankLogId(Integer bankLogId) {
		this.bankLogId = bankLogId;
		return this;
	}

	public Integer getRecordCnt() {
		return recordCnt;
	}

	public BankExportLog setRecordCnt(Integer recordCnt) {
		this.recordCnt = recordCnt;
		return this;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public BankExportLog setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public BigDecimal getTotalDebit() {
		return totalDebit;
	}

	public BankExportLog setTotalDebit(BigDecimal totalDebit) {
		this.totalDebit = totalDebit;
		return this;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public BankExportLog setTotalCredit(BigDecimal totalCredit) {
		this.totalCredit = totalCredit;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public BankExportLog setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
}
