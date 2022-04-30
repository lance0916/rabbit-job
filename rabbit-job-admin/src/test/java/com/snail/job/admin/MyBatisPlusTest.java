package com.snail.job.admin;

import com.snail.job.admin.model.App;
import com.snail.job.admin.service.AppService;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
                .setId(2L)
                .setDeleted(1);
        appService.updateById(app);
    }

}
