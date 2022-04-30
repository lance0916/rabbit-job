package com.snail.job.common.aspect;

import com.snail.job.common.constant.ServiceStatus;
import com.snail.job.common.model.ResultT;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * API 接口验签
 * @author WuQinglong
 */
@Aspect
@Component
public class ValidSignAspect {

    @Around("@annotation(com.snail.job.common.annotation.CheckSign)")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable {
        // 验证服务是否可用
        if (ServiceStatus.status == ServiceStatus.Status.STOPPING) {
            return ResultT.DOWN;
        }
        return joinPoint.proceed();
    }

}
