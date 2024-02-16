package io.github.batchservices.domain.global;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;

public class BatchFooter extends AbstractEntity implements EntityMarker {

	Integer recordType;
	Integer serviceClassCode;
	Integer entryAddendaCount;
	Integer entryHash;
	Integer totalDebitEntryDollarAmount;
	Integer totalCreditEntryDollarAmount;
	String companyIdentification;
	String unUsed1;
	String unUsed2;
	Integer originatingDFIIdentification;
	Integer batchNumber;
	
	public Integer getRecordType() {
		return recordType;
	}
	
	public BatchFooter setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}
	
	public Integer getServiceClassCode() {
		return serviceClassCode;
	}
	
	public BatchFooter setServiceClassCode(Integer serviceClassCode) {
		this.serviceClassCode = serviceClassCode;
		return this;
	}
	
	public Integer getEntryAddendaCount() {
		return entryAddendaCount;
	}
	
	public BatchFooter setEntryAddendaCount(Integer entryAddendaCount) {
		this.entryAddendaCount = entryAddendaCount;
		return this;
	}
	
	public Integer getEntryHash() {
		return entryHash;
	}
	
	public BatchFooter setEntryHash(Integer entryHash) {
		this.entryHash = entryHash;
		return this;
	}
	
	public Integer getTotalDebitEntryDollarAmount() {
		return totalDebitEntryDollarAmount;
	}
	
	public BatchFooter setTotalDebitEntryDollarAmount(Integer totalDebitEntryDollarAmount) {
		this.totalDebitEntryDollarAmount = totalDebitEntryDollarAmount;
		return this;
	}
	
	public Integer getTotalCreditEntryDollarAmount() {
		return totalCreditEntryDollarAmount;
	}
	
	public BatchFooter setTotalCreditEntryDollarAmount(Integer totalCreditEntryDollarAmount) {
		this.totalCreditEntryDollarAmount = totalCreditEntryDollarAmount;
		return this;
	}
	
	public String getCompanyIdentification() {
		return companyIdentification;
	}
	
	public BatchFooter setCompanyIdentification(String companyIdentification) {
		this.companyIdentification = companyIdentification;
		return this;
	}
	
	public String getUnUsed1() {
		return unUsed1;
	}
	
	public BatchFooter setUnUsed1(String unUsed1) {
		this.unUsed1 = unUsed1;
		return this;
	}
	
	public String getUnUsed2() {
		return unUsed2;
	}
	
	public BatchFooter setUnUsed2(String unUsed2) {
		this.unUsed2 = unUsed2;
		return this;
	}
	
	public Integer getOriginatingDFIIdentification() {
		return originatingDFIIdentification;
	}
	
	public BatchFooter setOriginatingDFIIdentification(Integer originatingDFIIdentification) {
		this.originatingDFIIdentification = originatingDFIIdentification;
		return this;
	}
	
	public Integer getBatchNumber() {
		return batchNumber;
	}
	
	public BatchFooter setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
		return this;
	}

	
		
}
