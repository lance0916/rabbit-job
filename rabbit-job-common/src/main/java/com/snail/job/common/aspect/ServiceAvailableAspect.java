package com.snail.job.common.aspect;

import com.snail.job.common.constant.HttpConstants;
import com.snail.job.common.filter.ServletRequestWrapper;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.tools.SignTool;
import com.snail.job.common.tools.SpringMVCTool;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * API 接口验签
 * @author WuQinglong
 */
@Aspect
@Component
public class ServiceAvailableAspect {

    @Value("${rabbit-job.admin.secret-key}")
    private String secretKey;

    @Around("@annotation(com.snail.job.common.annotation.CheckSign)")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = SpringMVCTool.getRequest();
        if (!(request instanceof ServletRequestWrapper)) {
            return new ResultT<>(ResultT.FAIL_CODE, "内部错误");
        }
        ServletRequestWrapper wrapper = (ServletRequestWrapper) request;

        // 请求是否超时
        String timestamp = wrapper.getHeader(HttpConstants.TIMESTAMP);
        long ts = Long.parseLong(timestamp);
        if (ts < System.currentTimeMillis() - 1000 * 10) {
            return new ResultT<>(ResultT.TIMEOUT_CODE, "请求超时");
        }

        // 验签
        byte[] body = wrapper.getBody();
        String bodyData = new String(body, StandardCharsets.UTF_8);
        String headerSign = wrapper.getHeader(HttpConstants.SIGN);
        String sign = SignTool.sign(bodyData, secretKey, timestamp);
        if (headerSign == null || !headerSign.equals(sign)) {
            return new ResultT<>(ResultT.SIGN_ERROR_CODE, "签名错误");
        }

        return joinPoint.proceed();
    }

}
