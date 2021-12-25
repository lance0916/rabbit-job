# 分布式定时任务调度系统

项目是参考 [xxl-job](https://github.com/xuxueli/xxl-job) 源码编写，只适用于 SpringBoot 项目，以 spring-boot-starter 集成到项目中的。

执行器端去除了 Netty 模块，不需要额外开端口，复用Web应用的端口进行任务调度。

# 快速开始

1、执行器引入jar包
```
<dependency>
    <groupId>com.snail</groupId>
    <artifactId>rabbit-job-spring-boot-starter</artifactId>
    <version>{latest.version}</version>
</dependency>
```

2、执行器接入配置
```
rabbit-job.executor.app-name=abc
# IP 和 Address 只需要配置一个即可 
#rabbit-job.executor.ip=127.0.0.1
rabbit-job.executor.address=http://127.0.0.1:8080
rabbit-job.admin-addresses=http://127.0.0.1:9001,http://127.0.0.1:9002,http://127.0.0.1:9003
rabbit-job.secret-key=hello
```

# 功能列表

- [x] 通过页面对任务进行 CRUD 操作
- [x] 动态修改任务状态，启动/停止，即时生效
- [x] 执行器动态注册，新执行器加入时，会即时生效
- [x] 调度策略：第一个、最后一个、故障转移、忙碌转移、一致性Hash、轮训、广播
- [x] 触发策略：Cron、人工触发
- [x] 调度过期策略：忽略、立即触发一次
- [x] 支持任务超时设置
- [x] 任务调度失败告警
- [x] 自定义任务参数
- [x] 调度中心与执行器之间的数据传输加密处理

# 规划

- [x] 执行器注册时，优先使用 address，而不是ip
- [x] 调度中心进行任务调度，执行器进行回调，都进行接口验签
- [x] 优化 client 回调 admin 时，对 admin 的选择算法，使用轮训，重试3次
- [ ] 调度中心HA
- [ ] 接入页面，增加账号体系
- [ ] 任务执行日志的记录和查看，并持久化存储。
- [ ] 服务端支持优雅停机，正在进行任务调度，需要等待调度完成再停机，不接受任务回调。
- [ ] 客户端支持优雅停机，不接受任务执行，回调完所有待回调的任务执行结果。
