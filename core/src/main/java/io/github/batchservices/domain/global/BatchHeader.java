package io.github.batchservices.domain.global;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;

public class BatchHeader extends AbstractEntity implements EntityMarker {

	Integer recordType;
	Integer serviceClassCode;	
	String companyName;	
	String companyDiscretionaryData;	
	String companyIdentification;	
	String standardEntryClass;	
	String companyEntryDescription;	
	String companyDescriptiveDate;	
	String effectiveEntryDate;		
	String settlementDate;	
	Integer originatorStatusCode;	
	Integer originatingDFIIdentification;	
	String batchNumber;
	
	public Integer getRecordType() {
		return recordType;
	}
	
	public BatchHeader setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}
	
	public Integer getServiceClassCode() {
		return serviceClassCode;
	}
	
	public BatchHeader setServiceClassCode(Integer serviceClassCode) {
		this.serviceClassCode = serviceClassCode;
		return this;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public BatchHeader setCompanyName(String companyName) {
		this.companyName = companyName;
		return this;
	}
	
	public String getCompanyDiscretionaryData() {
		return companyDiscretionaryData;		
	}
	
	public BatchHeader setCompanyDiscretionaryData(String companyDiscretionaryData) {
		this.companyDiscretionaryData = companyDiscretionaryData;
		return this;
	}
	
	public String getCompanyIdentification() {
		return companyIdentification;
	}
	
	public BatchHeader setCompanyIdentification(String companyIdentification) {
		this.companyIdentification = companyIdentification;
		return this;
	}
	
	public String getStandardEntryClass() {
		return standardEntryClass;
	}
	
	public BatchHeader setStandardEntryClass(String standardEntryClass) {
		this.standardEntryClass = standardEntryClass;
		return this;
	}
	
	public String getCompanyEntryDescription() {
		return companyEntryDescription;
	}
	
	public BatchHeader setCompanyEntryDescription(String companyEntryDescription) {
		this.companyEntryDescription = companyEntryDescription;
		return this;
	}
	
	public String getCompanyDescriptiveDate() {
		return companyDescriptiveDate;
	}
	
	public BatchHeader setCompanyDescriptiveDate(String companyDescriptiveDate) {
		this.companyDescriptiveDate = companyDescriptiveDate;
		return this;
	}
	
	public String getEffectiveEntryDate() {
		return effectiveEntryDate;
	}
	
	public BatchHeader setEffectiveEntryDate(String effectiveEntryDate) {
		this.effectiveEntryDate = effectiveEntryDate;
		return this;
	}
	
	public String getSettlementDate() {
		return settlementDate;
	}
	
	public BatchHeader setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
		return this;
		
	}
	
	public Integer getOriginatorStatusCode() {
		return originatorStatusCode;
	}
	
	public BatchHeader setOriginatorStatusCode(Integer originatorStatusCode) {
		this.originatorStatusCode = originatorStatusCode;
		return this;
	}
	
	public Integer getOriginatingDFIIdentification() {
		return originatingDFIIdentification;
	}
	
	public BatchHeader setOriginatingDFIIdentification(Integer originatingDFIIdentification) {
		this.originatingDFIIdentification = originatingDFIIdentification;
		return this;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}
	
	public BatchHeader setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
		return this;
	}
		
}
