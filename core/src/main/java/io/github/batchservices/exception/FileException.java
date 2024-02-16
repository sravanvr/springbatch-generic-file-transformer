package io.github.batchservices.exception;

import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.SeverityLevel;

public class FileException extends GenericFileTransformerException {

    public FileException(String message, BatchCode code) {
        super(message, code);
    }

    public FileException(String message, BatchCode code, SeverityLevel severityLevel) {
        super(message, code, severityLevel);
    }

    public FileException(String message, BatchCode code, SeverityLevel severityLevel, Throwable cause) {
        super(message, code, severityLevel, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    protected FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
