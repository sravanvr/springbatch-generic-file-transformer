package io.github.batchservices.exception;

import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.SeverityLevel;

public class ApplicationException extends GenericFileTransformerException {

    public ApplicationException(String message, BatchCode code) {
        super(message, code);
    }

    public ApplicationException(String message, BatchCode code, SeverityLevel severityLevel) {
        super(message, code, severityLevel);
    }

    public ApplicationException(String message, BatchCode code, SeverityLevel severityLevel, Throwable cause) {
        super(message, code, severityLevel, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    protected ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
