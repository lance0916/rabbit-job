package com.snail.job.common.thread;

import com.snail.job.common.tools.StrTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WuQinglong
 */
public class JobUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(JobUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("未捕获的异常：异常={}", StrTool.stringifyException(e));
    }

}
