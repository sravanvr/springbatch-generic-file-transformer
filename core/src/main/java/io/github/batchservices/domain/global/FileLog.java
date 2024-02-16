package io.github.batchservices.domain.global;

import java.sql.Timestamp;
import org.springframework.stereotype.Component;
import java.sql.Date;

public class FileLog {
	
	private Integer fileLogId;
	private Integer fileTypeId;
	private Integer dataTypeId;
	private Integer processStatusId;
	private String fileName;
	private Long fileSize;
	private Timestamp fileCreateDate;
	private Timestamp fileLastWriteDate;
	private Timestamp fileLastAccessDate;
	private Timestamp createDate;
	private Timestamp processingStartDate;
	private Timestamp processingCompleteDate;
	private String runstreamId;
	private String fileSequenceTxt;
	private Integer priority;
	private Date filePostDate;
	private Integer batchCountNbr;
	private Integer reprocessType;
	private String serverName;
	private String shortFileName;
	
	public FileLog() {
		super();
	}
	
	
	public FileLog(Integer fileLogId, Integer fileTypeId, Integer dataTypeId, Integer processStatusId, String fileName,
			Long fileSize, Timestamp fileCreateDate, Timestamp fileLastAccessDate, Timestamp createDate,
			Timestamp processingStartDate, Timestamp processingCompleteDate, String runstreamId, String fileSequenceTxt,
			Integer priority, Date filePostDate, Integer batchCountNbr, Integer reprocessType, String serverName,
			String shortFileName) {

		this.fileLogId = fileLogId;
		this.fileTypeId = fileTypeId;
		this.dataTypeId = dataTypeId;
		this.processStatusId = processStatusId;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileCreateDate = fileCreateDate;
		this.fileLastAccessDate = fileLastAccessDate;
		this.createDate = createDate;
		this.processingStartDate = processingStartDate;
		this.processingCompleteDate = processingCompleteDate;
		this.runstreamId = runstreamId;
		this.fileSequenceTxt = fileSequenceTxt;
		this.priority = priority;
		this.filePostDate = filePostDate;
		this.batchCountNbr = batchCountNbr;
		this.reprocessType = reprocessType;
		this.serverName = serverName;
		this.shortFileName = shortFileName;
	
	}
	
	
	public Integer getFileLogId() {
		return fileLogId;
	}
	
	public void setFileLogId(Integer fileLogId) {
		this.fileLogId = fileLogId;
	}
	
	public Integer getFileTypeId() {
		return fileTypeId;
	}
	
	public void setFileTypeId(Integer fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	
	public Integer getDataTypeId() {
		return dataTypeId;
	}
	
	public void setDataTypeId(Integer dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	public Integer getProcessStatusId() {
		return processStatusId;
	}
	
	public void setProcessStatusId(Integer processStatusId) {
		this.processStatusId = processStatusId;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public Timestamp getFileCreateDate() {
		return fileCreateDate;
	}
	
	public void setFileCreateDate(Timestamp fileCreateDate) {
		this.fileCreateDate = fileCreateDate;
	}
	
	public Timestamp getFileLastWriteDate() {
		return fileLastWriteDate;
	}


	public void setFileLastWriteDate(Timestamp fileLastWriteDate) {
		this.fileLastWriteDate = fileLastWriteDate;
	}


	public Timestamp getFileLastAccessDate() {
		return fileLastAccessDate;
	}
	
	public void setFileLastAccessDate(Timestamp fileLastAccessDate) {
		this.fileLastAccessDate = fileLastAccessDate;
	}
	
	public Timestamp getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	public Timestamp getProcessingStartDate() {
		return processingStartDate;
	}
	
	public void setProcessingStartDate(Timestamp processingStartDate) {
		this.processingStartDate = processingStartDate;
	}
	
	public Timestamp getProcessingCompleteDate() {
		return processingCompleteDate;
	}
	
	public void setProcessingCompleteDate(Timestamp processingCompleteDate) {
		this.processingCompleteDate = processingCompleteDate;
	}
	
	public String getRunstreamId() {
		return runstreamId;
	}
	
	public void setRunstreamId(String runstreamId) {
		this.runstreamId = runstreamId;
	}
	
	public String getFileSequenceTxt() {
		return fileSequenceTxt;
	}
	
	public void setFileSequenceTxt(String fileSequenceTxt) {
		this.fileSequenceTxt = fileSequenceTxt;
	}
	
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Date getFilePostDate() {
		return filePostDate;
	}
	
	public void setFilePostDate(Date filePostDate) {
		this.filePostDate = filePostDate;
	}
	
	public Integer getBatchCountNbr() {
		return batchCountNbr;
	}
	
	public void setBatchCountNbr(Integer batchCountNbr) {
		this.batchCountNbr = batchCountNbr;
	}
	
	public Integer getReprocessType() {
		return reprocessType;
	}
	
	public void setReprocessType(Integer reprocessType) {
		this.reprocessType = reprocessType;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public String getShortFileName() {
		return shortFileName;
	}


	public void setShortFileName(String shortFileName) {
		this.shortFileName = shortFileName;
	}




}
