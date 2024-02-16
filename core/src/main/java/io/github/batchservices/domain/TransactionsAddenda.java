package io.github.batchservices.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TransactionsAddenda extends AbstractDetailRecord implements EntityMarker {
	
	private Integer recordType;
	private Integer addendaType;
	private String paymentRelatedInformation;
	private Integer addendaSequenceNumber;
	private Integer entryDetailSequenceNumber;
	
	public Integer getRecordType() {
		return recordType;
	}
	
	public TransactionsAddenda setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}
	
	public Integer getAddendaType() {
		return addendaType;
	}
	
	public TransactionsAddenda setAddendaType(Integer addendaType) {
		this.addendaType = addendaType;
		return this;
	}
	
	public String getPaymentRelatedInformation() {
		return paymentRelatedInformation;
	}
	
	public TransactionsAddenda setPaymentRelatedInformation(String paymentRelatedInformation) {
		this.paymentRelatedInformation = paymentRelatedInformation;
		return this;
	}
	
	public Integer getAddendaSequenceNumber() {
		return addendaSequenceNumber;
	}
	
	public TransactionsAddenda setAddendaSequenceNumber(Integer addendaSequenceNumber) {
		this.addendaSequenceNumber = addendaSequenceNumber;
		return this;
	}
	
	public Integer getEntryDetailSequenceNumber() {
		return entryDetailSequenceNumber;
	}
	
	public TransactionsAddenda setEntryDetailSequenceNumber(Integer entryDetailSequenceNumber) {
		this.entryDetailSequenceNumber = entryDetailSequenceNumber;
		return this;
	}
	
}