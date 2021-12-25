# 分布式定时任务调度系统

项目是参考 [xxl-job](https://github.com/xuxueli/xxl-job) 源码编写，只适用于 SpringBoot 项目，是以 spring-boot-starter 集成到项目中的。

client 端去除了 Netty 模块，也不需要额外开端口了，复用 Web 的端口进行任务调度。

引入jar包：
```
<dependency>
    <groupId>com.snail</groupId>
    <artifactId>rabbit-job-spring-boot-starter</artifactId>
    <version>{latest.version}</version>
</dependency>
```

接入配置：
```
rabbit-job.executor.app-name=abc
# IP 和 Address 只需要配置一个即可 
#rabbit-job.executor.ip=127.0.0.1
rabbit-job.executor.address=http://127.0.0.1:8080
rabbit-job.admin-addresses=http://127.0.0.1:9001,http://127.0.0.1:9002,http://127.0.0.1:9003
rabbit-job.secret-key=hello
```

# 规划

- [x] 执行器注册时，优先使用 address，而不是ip
- [x] 调度中心进行任务调度，执行器进行回调，都进行接口验签
- [x] 优化 client 回调 admin 时，对 admin 的选择算法，使用轮训，重试3次
- [ ] 调度中心分布式部署
- [ ] 接入页面，增加账号体系，接入 SA-TOKEN 框架
- [ ] 任务执行日志的记录和查看
- [ ] 优雅停机时，正在进行任务调度，需要等待调度完成再停机
- [ ] 优雅停机时，不接受任务回调
