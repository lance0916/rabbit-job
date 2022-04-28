package com.snail.job.common.exception;

/**
 * @author 吴庆龙
 */
public class NoAdminAddressException extends RuntimeException {

    public NoAdminAddressException() {
        super();
    }

    public NoAdminAddressException(String message) {
        super(message);
    }

    public NoAdminAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAdminAddressException(Throwable cause) {
        super(cause);
    }

}
