package com.snail.job.common.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author WuQinglong
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

    /**
     * 判空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
