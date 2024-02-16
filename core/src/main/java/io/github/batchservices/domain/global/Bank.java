package io.github.batchservices.domain.global;

import java.io.Serializable;
import java.util.Date;

public class Bank implements Serializable {

    private String bankDomainName;
    private String bankID;
    private Integer bankNo;    
    private String DINo;
    private Integer checkDigit;
    private Integer deactivated;
    private Integer fileFormat;
    private String fileVersion;
    private String importPath;
    private Date lastImport;
    private String listImportFiles;
	private Integer priorityLevel;
    private String serverName;
    private Integer incrementalTransactionsFlag;
    private Integer ccfid;

	public Bank() {
    	super();
    }
    
    public Bank(String bankDomainName, String bankID, Integer bankNo, String dINo, Integer checkDigit,
 			Integer deactivated, Integer fileFormat, String fileVersion, String importPath, Date lastImport,
 			String listImportFiles, Integer priorityLevel, String serverName, Integer incrementalTransactionsFlag) {
 		super();
 		this.bankDomainName = bankDomainName;
 		this.bankID = bankID;
 		this.bankNo = bankNo;
 		DINo = dINo;
 		this.checkDigit = checkDigit;
 		this.deactivated = deactivated;
 		this.fileFormat = fileFormat;
 		this.fileVersion = fileVersion;
 		this.importPath = importPath;
 		this.lastImport = lastImport;
 		this.listImportFiles = listImportFiles;
 		this.priorityLevel = priorityLevel;
 		this.serverName = serverName;
 		this.incrementalTransactionsFlag = incrementalTransactionsFlag;
 	}
    
    public String getBankDomainName() {
        return bankDomainName;
    }

    public Bank setBankDomainName(String bankDomainName) {
        this.bankDomainName = bankDomainName;
        return this;
    }

    public String getBankID() {
        return bankID;
    }

    public Bank setBankID(String bankID) {
        this.bankID = bankID;
        return this;
    }

    public Integer getBankNo() {
        return bankNo;
    }

    public Bank setBankNo(Integer bankNo) {
        this.bankNo = bankNo;
        return this;
    }

    public Integer getDeactivated() {
        return deactivated;
    }

    public Bank setDeactivated(Integer deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    public String getDINo() {
        return DINo;
    }

    public Bank setDINo(String DINo) {
        this.DINo = DINo;
        return this;
    }

    public Integer getFileFormat() {
        return fileFormat;
    }

    public Bank setFileFormat(Integer fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public Bank setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
        return this;
    }


    public String getImportPath() {
        return importPath;
    }

    public Bank setImportPath(String importPath) {
        this.importPath = importPath;
        return this;
    }

    public Date getLastImport() {
        return lastImport;
    }

    public Bank setLastImport(Date lastImport) {
        this.lastImport = lastImport;
        return this;
    }

    public String getListImportFiles() {
        return listImportFiles;
    }

    public Bank setListImportFiles(String listImportFiles) {
        this.listImportFiles = listImportFiles;
        return this;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public Bank setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public Bank setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

	public Integer getCheckDigit() {
		return checkDigit;
	}

	public Bank setCheckDigit(Integer checkDigit) {
		this.checkDigit = checkDigit;
		return this;
	}

	public Integer getIncrementalTransactionsFlag() {
		return incrementalTransactionsFlag;

	}

	public void setIncrementalTransactionsFlag(Integer incrementalTransactionsFlag) {
		this.incrementalTransactionsFlag = incrementalTransactionsFlag;
	}

    public Integer getCcfid() {
		return ccfid;
	}
    
	public void setCcfid(Integer ccfid) {
		this.ccfid = ccfid;
	}
}