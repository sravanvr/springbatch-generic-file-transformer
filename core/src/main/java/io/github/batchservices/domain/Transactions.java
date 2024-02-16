package io.github.batchservices.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Transactions extends AbstractDetailRecord implements EntityMarker {
	private Integer recordType;	
	private Integer transactionCode;	
	private Integer dfiIdentification;	
	private Integer checkDigit;	
	private String accountNumber;	
	private Integer transactionAmount;	
	private String identificationNumber;	
	private String individualName;	
	private String discretionaryData;	
	private Integer addendaIndicator;	
	private Integer originatorTraceNumber;	
	private Integer traceNumber;
	
	public Integer getRecordType() {
		return recordType;
	}
	
	public Transactions setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}
	
	public Integer getTransactionCode() {
		return transactionCode;
	}
	
	public Transactions setTransactionCode(Integer transactionCode) {
		this.transactionCode = transactionCode;
		return this;
	}
	
	public Integer getDfiIdentification() {
		return dfiIdentification;
	}
	
	public Transactions setDfiIdentification(Integer dfiIdentification) {
		this.dfiIdentification = dfiIdentification;
		return this;
	}
	
	public Integer getCheckDigit() {
		return checkDigit;
	}
	
	public Transactions setCheckDigit(Integer checkDigit) {
		this.checkDigit = checkDigit;
		return this;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public Transactions setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}
	
	public Integer getTransactionAmount() {
		return transactionAmount;
	}
	
	public Transactions setTransactionAmount(Integer transactionAmount) {
		this.transactionAmount = transactionAmount;
		return this;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public Transactions setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
		return this;
	}
	
	public String getIndividualName() {
		return individualName;
	}
	
	public Transactions setIndividualName(String individualName) {
		this.individualName = individualName;
		return this;
	}
	
	public String getDiscretionaryData() {
		return discretionaryData;
	}
	
	public Transactions setDiscretionaryData(String discretionaryData) {
		this.discretionaryData = discretionaryData;
		return this;
	}
	
	public Integer getAddendaIndicator() {
		return addendaIndicator;
	}
	
	public Transactions setAddendaIndicator(Integer addendaIndicator) {
		this.addendaIndicator = addendaIndicator;
		return this;
	}
	
	public Integer getOriginatorTraceNumber() {
		return originatorTraceNumber;
	}
	
	public Transactions setOriginatorTraceNumber(Integer originatorTraceNumber) {
		this.originatorTraceNumber = originatorTraceNumber;
		return this;
	}
	
	public Integer getTraceNumber() {
		return traceNumber;
	}
	
	public Transactions setTraceNumber(Integer traceNumber) {
		this.traceNumber = traceNumber;
		return this;
	}
	
}