package com.snail.job.admin;

import cn.hutool.system.*;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.jupiter.api.Test;
import oshi.software.os.OSProcess;

/**
 * @author WuQinglong created on 2021/12/18 16:23
 */
public class DemoTest {

    @Test
    public void f() throws InterruptedException {
        JavaRuntimeInfo javaRuntimeInfo = new JavaRuntimeInfo();
        System.out.println(javaRuntimeInfo);
        System.out.println("==============");
        System.out.println(new JavaInfo());
        System.out.println("==============");
        System.out.println(new HostInfo());
        System.out.println("==============");
        System.out.println(new JvmInfo());
        System.out.println("==============");
//        for (int i = 0; i < 10; i++) {
//            System.out.println(OshiUtil.getCpuInfo());
//            System.out.println(OshiUtil.getCpuInfo().getTicks());
//            Thread.sleep(1000);
//        }
        System.out.println("==============");
        System.out.println(new UserInfo());
        System.out.println("==============");
        System.out.println(new RuntimeInfo());

        OSProcess process = OshiUtil.getCurrentProcess();
        System.out.println(process.getCommandLine());
        System.out.println(process.getName());
        System.out.println(process.getPath());
        System.out.println(process.getProcessID());
        System.out.println(process.getStartTime());
        System.out.println(process.getUpTime());
        System.out.println(process.getThreadCount());

//        DataSizeUtil.format(size);

    }

}
