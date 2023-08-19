package com.example.common.aspect;

import com.example.common.constant.ServiceStatus;
import com.example.common.model.ResultT;
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
public class ServiceAvailableAspect {

    @Around("@annotation(com.example.common.aspect.annotation.CheckSign)")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable {
        // 验证服务是否可用
        if (ServiceStatus.status == ServiceStatus.Status.STOPPING) {
            return ResultT.DOWN;
        }

        return joinPoint.proceed();
    }

}
