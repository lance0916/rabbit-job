package com.example.common;

import com.example.common.tools.StrTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WuQinglong
 */
public abstract class RabbitJobAbstractThread {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 线程对象
     */
    protected Thread thread;

    /**
     * 线程运行标志
     */
    protected volatile boolean running = true;

    /**
     * 获取线程名称
     * @return 线程名称
     */
    protected String getThreadName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 设置线程的优先级
     * @return 优先级
     */
    protected int getPriority() {
        return Thread.NORM_PRIORITY;
    }

    /**
     * 对齐到整秒
     */
    private void alignSecond() {
        long curMillis = System.currentTimeMillis();

        // 已经过了 xxx 毫秒
        long sleepMillis = curMillis % 1000;

        // 需要休眠 1000 - xxx 毫秒
        try {
            Thread.sleep(1000 - sleepMillis);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 执行前
     */
    protected void before() throws Exception {
    }

    /**
     * 线程具体执行的方法
     * @throws InterruptedException 被中断时抛异常
     */
    protected abstract void execute() throws InterruptedException;

    /**
     * 每次 run 之后执行
     * 在 finally 中执行，不受异常影响
     */
    protected void afterExecute() {
    }

    /**
     * 后置方法
     */
    protected void after() throws Exception {
    }

    /**
     * 启动线程
     */
    public void start() {
        thread = new Thread(() -> {
            // 先对齐到整秒
            alignSecond();

            // 执行前置操作
            try {
                before();
            } catch (Exception e) {
                log.error("执行前置操作异常！{}", StrTool.stringifyException(e));
                return;
            }

            // 执行逻辑
            while (running) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    if (running) {
                        log.warn("线程被中断。{}", StrTool.stringifyException(e));
                    }

                    // 线程被中断，退出 while 循环
                    break;
                } catch (Exception e) {
                    log.error("执行异常！{}", StrTool.stringifyException(e));
                } finally {
                    afterExecute();
                }
            }

            // 执行后置操作
            try {
                after();
            } catch (Exception e) {
                log.error("执行后置操作异常！{}", StrTool.stringifyException(e));
            }
        });
        thread.setDaemon(true);
        thread.setName(getThreadName());
        thread.setPriority(getPriority());
        thread.setUncaughtExceptionHandler(new JobUncaughtExceptionHandler());
        thread.start();
    }

    /**
     * 停止线程
     */
    public void stop() {
        // 停止标识，退出 while 循环
        running = false;

        // 设置中断标志
        thread.interrupt();

        // 先执行后置逻辑
        try {
            thread.join();
        } catch (Exception e) {
            log.error("停止线程异常。异常={}", StrTool.stringifyException(e));
        }
    }

}
