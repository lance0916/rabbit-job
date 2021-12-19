package com.snail.job.admin;

import cn.hutool.system.*;
import cn.hutool.system.oshi.OshiUtil;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.tools.GsonTool;
import org.junit.jupiter.api.Test;
import oshi.software.os.OSProcess;

import java.time.LocalDateTime;

/**
 * @author WuQinglong created on 2021/12/18 16:23
 */
public class DemoTest {

    @Test
    public void f() throws InterruptedException {

        CallbackParam param = new CallbackParam();
        param.setBeginExecTime(LocalDateTime.now());

        System.out.println(GsonTool.toJson(param));

    }

}
