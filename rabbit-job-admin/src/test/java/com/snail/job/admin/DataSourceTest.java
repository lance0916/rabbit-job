package com.snail.job.admin;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author WuQinglong created on 2021/12/13 08:27
 */
@SpringBootTest(
        classes = RabbitJobAdminApplication.class
)
@AutoConfigureMockMvc
public class DataSourceTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void testF() {
        System.out.println(dataSource);
        System.out.println(dataSource instanceof HikariDataSource);

        if (dataSource instanceof HikariDataSource) {
            HikariDataSource ds = (HikariDataSource) dataSource;
            System.out.println(ds.getMinimumIdle());
            System.out.println(ds.getMaximumPoolSize());
        }
    }

}
