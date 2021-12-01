package com.snail.job.common.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author WuQinglong
 * @date 2021/9/7 2:02 下午
 */
public class StrTool {

    /**
     * 获取异常信息
     */
    public static String stringifyException(Throwable e) {
        StringWriter stm = new StringWriter();
        PrintWriter wrt = new PrintWriter(stm);
        e.printStackTrace(wrt);
        wrt.close();
        return stm.toString();
    }

}
