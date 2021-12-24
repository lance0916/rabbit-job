package com.snail.job.common.exception;

/**
 * @author 吴庆龙
 * @date 2020/7/23 2:34 下午
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
