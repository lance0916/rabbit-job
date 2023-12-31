package com.example.common.exception;

/**
 * @author WuQinglong
 */
public class RegisterJobException extends RuntimeException {

    public RegisterJobException() {
        super();
    }

    public RegisterJobException(String message) {
        super(message);
    }

    public RegisterJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterJobException(Throwable cause) {
        super(cause);
    }

}
