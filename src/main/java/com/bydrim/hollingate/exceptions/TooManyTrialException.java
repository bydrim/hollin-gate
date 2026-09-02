package com.bydrim.hollingate.exceptions;

import java.sql.SQLException;

public class TooManyTrialException extends SQLException {
    public TooManyTrialException() {
    }

    public TooManyTrialException(String reason) {
        super(reason);
    }

    public TooManyTrialException(Throwable cause) {
        super(cause);
    }

    public TooManyTrialException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public TooManyTrialException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public TooManyTrialException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }

    public TooManyTrialException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public TooManyTrialException(String reason, String SQLState) {
        super(reason, SQLState);
    }
}
