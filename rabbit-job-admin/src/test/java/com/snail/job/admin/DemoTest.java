package com.snail.job.admin;

import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.tools.GsonTool;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

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
