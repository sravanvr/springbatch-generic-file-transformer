package io.github.batchservices.domain;

import io.github.batchservices.domain.global.ErrorLog;
import io.github.batchservices.util.SeverityLevel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractEntity {

    @Transient
    private SeverityLevel recordState;

    @Transient
    private Integer recordType;

    @Transient
    private int rowNumber;

    @Transient
    private List<ErrorLog> errorLog = new ArrayList<>();

    @Column(name="file_log_id")
    private Integer fileLogId;

    public Integer getFileLogId() {
        return fileLogId;
    }

    public AbstractEntity setFileLogId(Integer fileLogId) {
        this.fileLogId = fileLogId;
        return this;
    }

    public SeverityLevel getRecordState() {
        return recordState;
    }

    public AbstractEntity setRecordState(SeverityLevel recordState) {
        this.recordState = recordState;
        return this;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public AbstractEntity setRecordType(Integer recordType) {
        this.recordType = recordType;
        return this;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public AbstractEntity setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
        return this;
    }

    public List<ErrorLog> getErrorLog() {
        return errorLog;
    }

    public AbstractEntity setErrorLog(List<ErrorLog> errorLog) {
        this.errorLog = errorLog;
        return this;
    }
}
