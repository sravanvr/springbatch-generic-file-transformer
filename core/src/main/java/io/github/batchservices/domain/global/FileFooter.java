package io.github.batchservices.domain.global;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;

public class FileFooter extends AbstractEntity implements EntityMarker {

	Integer recordType;
	Integer batchCount;
	Integer blockCount;
	Integer entryAddendaCount;
	Integer entryHash;	
	Integer totalDebitEntryDollarAmount;	
	Integer totalCreditEntryDollarAmount;	
	String unUsed;

	public Integer getRecordType() {
		return recordType;
	}

	public FileFooter setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}

	public Integer getBatchCount() {
		return batchCount;
	}

	public FileFooter setBatchCount(Integer batchCount) {
		this.batchCount = batchCount;
		return this;
	}

	public Integer getBlockCount() {
		return blockCount;
	}

	public FileFooter setBlockCount(Integer blockCount) {
		this.blockCount = blockCount;
		return this;
	}

	public Integer getEntryAddendaCount() {
		return entryAddendaCount;
	}

	public FileFooter setEntryAddendaCount(Integer entryAddendaCount) {
		this.entryAddendaCount = entryAddendaCount;
		return this;
	}

	public Integer getEntryHash() {
		return entryHash;
	}

	public FileFooter setEntryHash(Integer entryHash) {
		this.entryHash = entryHash;
		return this;
	}

	public Integer getTotalDebitEntryDollarAmount() {
		return totalDebitEntryDollarAmount;
	}

	public FileFooter setTotalDebitEntryDollarAmount(Integer totalDebitEntryDollarAmount) {
		this.totalDebitEntryDollarAmount = totalDebitEntryDollarAmount;
		return this;
	}

	public Integer getTotalCreditEntryDollarAmount() {
		return totalCreditEntryDollarAmount;
	}

	public FileFooter setTotalCreditEntryDollarAmount(Integer totalCreditEntryDollarAmount) {
		this.totalCreditEntryDollarAmount = totalCreditEntryDollarAmount;
		return this;
	}

	public String getUnUsed() {
		return unUsed;
	}

	public FileFooter setUnUsed(String unUsed) {
		this.unUsed = unUsed;
		return this;
	}
	
	
}
