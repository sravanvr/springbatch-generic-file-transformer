package io.github.batchservices.domain.global;

import io.github.batchservices.domain.AbstractEntity;
import io.github.batchservices.domain.EntityMarker;

public class FileHeader extends AbstractEntity implements EntityMarker {

	Integer recordType;
	Integer priorityCode;
	Integer immediateDestination;
	Integer immediateOrigin;
	Integer fileCreationDate;
	Integer fileCreationTime;
	String fileIDModifier;
	Integer recordSize;
	Integer blockingFactor;
	Integer formatCode;
	String immediateDestinationName;
	String immediateOriginName;
	String referenceCode;
	
	public Integer getRecordType() {
		return recordType;
	}
	
	public FileHeader setRecordType(Integer recordType) {
		this.recordType = recordType;
		return this;
	}
	
	public Integer getPriorityCode() {
		return priorityCode;
	}
	
	public FileHeader setPriorityCode(Integer priorityCode) {
		this.priorityCode = priorityCode;
		return this;
	}
	
	public Integer getImmediateDestination() {
		return immediateDestination;
	}
	
	public FileHeader setImmediateDestination(Integer immediateDestination) {
		this.immediateDestination = immediateDestination;
		return this;
	}
	
	public Integer getImmediateOrigin() {
		return immediateOrigin;
	}
	
	public FileHeader setImmediateOrigin(Integer immediateOrigin) {
		this.immediateOrigin = immediateOrigin;
		return this;
	}
	
	public Integer getFileCreationDate() {
		return fileCreationDate;
	}
	
	public FileHeader setFileCreationDate(Integer fileCreationDate) {
		this.fileCreationDate = fileCreationDate;
		return this;
	}
	
	public Integer getFileCreationTime() {
		return fileCreationTime;
	}
	
	public FileHeader setFileCreationTime(Integer fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
		return this;
	}
	
	public String getFileIDModifier() {
		return fileIDModifier;
	}
	
	public FileHeader setFileIDModifier(String fileIDModifier) {
		this.fileIDModifier = fileIDModifier;
		return this;
	}
	
	public Integer getRecordSize() {
		return recordSize;
	}
	
	public FileHeader setRecordSize(Integer recordSize) {
		this.recordSize = recordSize;
		return this;
	}
	
	public Integer getBlockingFactor() {
		return blockingFactor;
	}
	
	public FileHeader setBlockingFactor(Integer blockingFactor) {
		this.blockingFactor = blockingFactor;
		return this;
	}
	
	public Integer getFormatCode() {
		return formatCode;
	}
	
	public FileHeader setFormatCode(Integer formatCode) {
		this.formatCode = formatCode;
		return this;
	}
	
	public String getImmediateDestinationName() {
		return immediateDestinationName;
	}
	
	public FileHeader setImmediateDestinationName(String immediateDestinationName) {
		this.immediateDestinationName = immediateDestinationName;
		return this;
	}
	
	public String getImmediateOriginName() {
		return immediateOriginName;
	}
	
	public FileHeader setImmediateOriginName(String immediateOriginName) {
		this.immediateOriginName = immediateOriginName;
		return this;
	}
	
	public String getReferenceCode() {
		return referenceCode;
	}
	
	public FileHeader setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
		return this;
	}
	
}
