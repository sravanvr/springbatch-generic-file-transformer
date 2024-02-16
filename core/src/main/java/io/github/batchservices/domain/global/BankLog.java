package io.github.batchservices.domain.global;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class BankLog {
	
	private Integer fileLogId;
	private Integer bankLogId;
	private String bankDomainName;
	private Integer processStatusId;
	private Date createDate;
	private Date processingStartDate;
	private Date effectiveDate;
	private Date completeDate;
	private String batchSequenceTxt;
	private Integer reprocessType;
	private Date asOfDate;
	
	public BankLog() {
		super();
	}

	public BankLog(Integer fileLogId, Integer bankLogId, String bankDomainName, Integer processStatusId,
			Date createDate, Date effectiveDate, Date completeDate, String batchSequenceTxt,
			Integer reprocessType, Date asOfDate) {
		super();
		this.fileLogId = fileLogId;
		this.bankLogId = bankLogId;
		this.bankDomainName = bankDomainName;
		this.processStatusId = processStatusId;
		this.createDate = createDate;
		this.effectiveDate = effectiveDate;
		this.completeDate = completeDate;
		this.batchSequenceTxt = batchSequenceTxt;
		this.reprocessType = reprocessType;
		this.asOfDate = asOfDate;
	}

	public Integer getFileLogId() {
		return fileLogId;
	}

	public BankLog setFileLogId(Integer fileLogId) {
		this.fileLogId = fileLogId;
		return this;
	}

	public Integer getBankLogId() {
		return bankLogId;
	}

	public BankLog setBankLogId(Integer bankLogId) {
		this.bankLogId = bankLogId;
		return this;
	}

	public String getBankDomainName() {
		return bankDomainName;
	}

	public BankLog setBankDomainName(String bankDomainName) {
		this.bankDomainName = bankDomainName;
		return this;
	}

	public Integer getProcessStatusId() {
		return processStatusId;
	}

	public BankLog setProcessStatusId(Integer processStatusId) {
		this.processStatusId = processStatusId;
		return this;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public BankLog setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public Date getProcessingStartDate() {
		return processingStartDate;
	}

	public BankLog setProcessingStartDate(Date processingStartDate) {
		this.processingStartDate = processingStartDate;
		return this;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public BankLog setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
		return this;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public BankLog setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
		return this;
	}

	public String getBatchSequenceTxt() {
		return batchSequenceTxt;
	}

	public BankLog setBatchSequenceTxt(String batchSequenceTxt) {
		this.batchSequenceTxt = batchSequenceTxt;
		return this;
	}

	public Integer getReprocessType() {
		return reprocessType;
	}

	public BankLog setReprocessType(Integer reprocessType) {
		this.reprocessType = reprocessType;
		return this;
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public BankLog setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
		return this;
	}
}
