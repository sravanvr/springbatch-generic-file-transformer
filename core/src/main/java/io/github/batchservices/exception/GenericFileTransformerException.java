package io.github.batchservices.exception;

import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.SeverityLevel;

public class GenericFileTransformerException extends RuntimeException {

    private SeverityLevel severityLevel;
    private BatchCode code;

    public GenericFileTransformerException() {
        super();
    }

    public GenericFileTransformerException(String message, BatchCode code) {
    	super(message);
        this.code = code;
    }

    public GenericFileTransformerException(String message, BatchCode code, SeverityLevel severityLevel) {
        super(message);
        this.severityLevel = severityLevel;
        this.code = code;
    }

    public GenericFileTransformerException(String message, BatchCode code, SeverityLevel severityLevel, Throwable cause) {
        super(message, cause);
        this.severityLevel = severityLevel;
        this.code = code;
    }

    public GenericFileTransformerException(Throwable cause) {
        super(cause);
        this.severityLevel = SeverityLevel.FATAL;
    }

    protected GenericFileTransformerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SeverityLevel getSeverityLevel() {
        return severityLevel;
    }

    public GenericFileTransformerException setSeverityLevel(SeverityLevel severityLevel) {
        this.severityLevel = severityLevel;
        return this;
    }

    public BatchCode getCode() {
        return code;
    }

    public GenericFileTransformerException setCode(BatchCode code) {
        this.code = code;
        return this;
    }
}
