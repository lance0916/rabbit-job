package com.example.common.exception;

/**
 * @author WuQinglong
 */
public class RabbitJobException extends RuntimeException {

    public RabbitJobException() {
        super();
    }

    public RabbitJobException(String message) {
        super(message);
    }

    public RabbitJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitJobException(Throwable cause) {
        super(cause);
    }

}
