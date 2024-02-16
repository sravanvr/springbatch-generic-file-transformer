package io.github.batchservices.domain.global;

import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileProperties {

	String filePath;
	String fileName;
	String runstreamId;
	String dataTypeId;
	String fileSequence;
	Date fileDate;
	String fileMM;
	String fileDD;
	FileTime fileCreationTime;
	FileTime fileLastAccessTime;
	FileTime fileLastModifiedTime;
	Long fileSize;
	
	public FileProperties() {};

	public FileProperties(String filePath, String fileName, String runstreamId, String dataTypeId, String fileSequence,
			Date fileDate, String fileMM, String fileDD, FileTime fileCreationTime, FileTime fileLastAccessTime,
			FileTime fileLastModifiedTime, Long fileSize) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.runstreamId = runstreamId;
		this.dataTypeId = dataTypeId;
		this.fileSequence = fileSequence;
		this.fileDate = fileDate;
		this.fileMM = fileMM;
		this.fileDD = fileDD;
		this.fileCreationTime = fileCreationTime;
		this.fileLastAccessTime = fileLastAccessTime;
		this.fileLastModifiedTime = fileLastModifiedTime;
		this.fileSize = fileSize;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRunstreamId() {
		return runstreamId;
	}
	public void setRunstreamId(String runstreamId) {
		this.runstreamId = runstreamId;
	}
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public String getFileSequence() {
		return fileSequence;
	}
	public void setFileSequence(String fileSequence) {
		this.fileSequence = fileSequence;
	}
	public Date getFileDate() {
		return fileDate;
	}
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}
	public FileTime getFileCreationTime() {
		return fileCreationTime;
	}
	public void setFileCreationTime(FileTime fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
	}
	public FileTime getFileLastAccessTime() {
		return fileLastAccessTime;
	}
	public void setFileLastAccessTime(FileTime fileLastAccessTime) {
		this.fileLastAccessTime = fileLastAccessTime;
	}
	public FileTime getFileLastModifiedTime() {
		return fileLastModifiedTime;
	}
	public void setFileLastModifiedTime(FileTime fileLastModifiedTime) {
		this.fileLastModifiedTime = fileLastModifiedTime;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMM() {
		return fileMM;
	}

	public void setFileMM(String fileMM) {
		this.fileMM = fileMM;
	}

	public String getFileDD() {
		return fileDD;
	}

	public void setFileDD(String fileDD) {
		this.fileDD = fileDD;
	}


	
	
}
