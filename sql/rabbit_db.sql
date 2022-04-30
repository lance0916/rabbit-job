CREATE DATABASE IF NOT EXISTS `rabbit_job` DEFAULT CHARACTER SET utf8mb4;
USE `rabbit_job`;

-- 注册信息
DROP TABLE IF EXISTS `executor`;
CREATE TABLE IF NOT EXISTS `executor`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `app_name`    VARCHAR(20) NOT NULL COMMENT '关联应用',
    `address`     VARCHAR(30) NOT NULL COMMENT '执行器地址',
    `deleted`     INT         NOT NULL DEFAULT 0 COMMENT '是否删除',
    `create_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 应用
DROP TABLE IF EXISTS `app`;
CREATE TABLE IF NOT EXISTS `app`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(20)  NOT NULL COMMENT '唯一名称',
    `title`       VARCHAR(50)  NOT NULL COMMENT '标题',
    `type`        INT          NOT NULL COMMENT '注册类型。自动注册=0；手动注册=1',
    `addresses`   VARCHAR(100) NOT NULL DEFAULT '' COMMENT '执行器地址',
    `deleted`     INT          NOT NULL DEFAULT 0 COMMENT '是否删除',
    `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 任务信息
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE IF NOT EXISTS `job_info`
(
    `id`                    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`                  VARCHAR(100) NOT NULL COMMENT '任务名称',
    `app_name`              VARCHAR(20)  NOT NULL COMMENT '应用名称',
    `cron`                  VARCHAR(20)  NOT NULL COMMENT 'CRON表达式',
    `deleted`               INT          NOT NULL DEFAULT 0 COMMENT '是否删除',
    `create_time`           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

    `author_name`           VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '负责人姓名',
    `author_email`          VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '负责人邮箱',

    `exec_route_strategy`   VARCHAR(50)  NOT NULL COMMENT '执行路由策略',
    `exec_handler`          VARCHAR(50)  NOT NULL COMMENT '执行任务handler',
    `exec_param`            VARCHAR(100) NOT NULL DEFAULT '' COMMENT '执行任务参数',
    `exec_timeout`          INT          NOT NULL DEFAULT 0 COMMENT '执行超时时间，单位秒',
    `exec_fail_retry_count` INT          NOT NULL DEFAULT 0 COMMENT '失败重试次数',

    `trigger_status`        INT          NOT NULL DEFAULT 0 COMMENT '调度状态。0-停止，1-运行',
    `trigger_prev_time`     BIGINT       NULL COMMENT '上次调度时间',
    `trigger_next_time`     BIGINT       NULL COMMENT '下次调度时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 任务日志
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE IF NOT EXISTS `job_log`
(
    `id`               BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `job_id`           BIGINT        NOT NULL COMMENT '任务，主键ID',
    `app_name`         VARCHAR(20)   NOT NULL COMMENT '任务组名',
    `create_time`      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    `exec_address`     VARCHAR(50)   NULL COMMENT '执行-地址',
    `exec_handler`     VARCHAR(50)   NULL COMMENT '执行-handler',
    `exec_param`       VARCHAR(100)  NULL COMMENT '执行-参数',
    `fail_retry_count` INT           NOT NULL DEFAULT 0 COMMENT '执行-失败重试次数',

    `trigger_time`     TIMESTAMP     NULL COMMENT '调度-时间',
    `trigger_type`     VARCHAR(50)   NULL COMMENT '调度-类型',
    `trigger_code`     INT           NOT NULL DEFAULT 0 COMMENT '调度-结果码',
    `trigger_msg`      VARCHAR(1024) NULL COMMENT '调度-结果信息',

    `exec_code`        INT           NOT NULL DEFAULT 0 COMMENT '执行-结果码',
    `exec_msg`         VARCHAR(1024) NULL COMMENT '执行-结果信息',
    `exec_begin_time`  TIMESTAMP     NULL COMMENT '执行-开始时间',
    `exec_end_time`    TIMESTAMP     NULL COMMENT '执行-结束时间',
    `exec_cost_time`   INT           NULL COMMENT '执行-耗时',

    `alarm_status`     INT           NOT NULL DEFAULT 0 COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 首页任务执行报告
DROP TABLE IF EXISTS `job_log_report`;
CREATE TABLE IF NOT EXISTS `job_log_report`
(
    `id`            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `trigger_date`  DATE   NOT NULL COMMENT '调度日期',
    `running_count` INT    NOT NULL DEFAULT 0 COMMENT '运行中-日志数量',
    `success_count` INT    NOT NULL DEFAULT 0 COMMENT '执行成功-日志数量',
    `fail_count`    INT    NOT NULL DEFAULT 0 COMMENT '执行失败-日志数量',
    UNIQUE KEY `uk_td` (`trigger_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- lock
DROP TABLE IF EXISTS `lock`;
CREATE TABLE IF NOT EXISTS `lock`
(
    `lock_key` VARCHAR(50) NOT NULL COMMENT 'lockKey'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
INSERT INTO `lock`(`lock_key`)
VALUES ('schedule_lock')
