package io.github.batchservices.domain.global;

public class ErrorLog {

	private Integer fileLogId;
	private Integer bankLogId;
	private Integer severityLevelId;
	private Integer errorLogId;
	private String errMessage;
	private Integer recordNum;


	public ErrorLog() {
		super();
	}
	
	public ErrorLog(Integer fileLogId, Integer bankLogId, Integer severityLevelId, Integer errorLogId,
			String errMessage, Integer recordNum) {
		super();
		this.fileLogId = fileLogId;
		this.bankLogId = bankLogId;
		this.severityLevelId = severityLevelId;
		this.errorLogId = errorLogId;
		this.errMessage = errMessage;
		this.recordNum = recordNum;
	}

	public Integer getFileLogId() {
		return fileLogId;
	}
	public void setFileLogId(Integer fileLogId) {
		this.fileLogId = fileLogId;
	}
	public Integer getBankLogId() {
		return bankLogId;
	}
	public void setBankLogId(Integer bankLogId) {
		this.bankLogId = bankLogId;
	}
	public Integer getSeverityLevelId() {
		return severityLevelId;
	}
	public void setSeverityLevelId(Integer severityLevelId) {
		this.severityLevelId = severityLevelId;
	}
	public Integer getErrorLogId() {
		return errorLogId;
	}
	public void setErrorLogId(Integer errorLogId) {
		this.errorLogId = errorLogId;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public Integer getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(Integer recordNum) {
		this.recordNum = recordNum;
	}
	
	@Override
	public String toString() {
		return "ErrorLog{" +
		        "fileLogId='" + fileLogId + '\'' +
		        ", bankLogId='" + bankLogId + '\'' +
		        ", severityLevelId=" + severityLevelId+ '\'' +
		        ", errorLogId=" + errorLogId + '\'' +
		        ", errMessage=" + errMessage + '\'' +		        
		        ", recordNum=" + recordNum +
		        '}';
	}
}
