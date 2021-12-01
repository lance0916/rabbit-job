package com.snail.job.common.exception;

/**
 * @author 吴庆龙
 * @date 2020/7/23 2:34 下午
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
