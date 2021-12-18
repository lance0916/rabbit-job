package com.snail.job.admin;

import com.snail.job.admin.model.App;
import com.snail.job.admin.service.AppService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author WuQinglong created on 2021/12/18 14:57
 */
@SpringBootTest
public class MyBatisPlusTest {

    @Resource
    private AppService appService;

    @Test
    public void testUpdate() {
        App app = new App()
                .setId(1L)
                .setDeleted(1)
                .setUpdateTime(LocalDateTime.now());
        appService.updateById(app);
    }

}
