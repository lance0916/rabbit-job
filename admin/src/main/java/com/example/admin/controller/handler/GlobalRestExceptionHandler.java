package com.example.admin.controller.handler;

import com.example.common.model.ResultT;
import com.example.common.tools.StrTool;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller 发生 Exception 异常时，进行捕捉并返回执行异常信息
 * @author WuQinglong
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    /**
     * 返回 Validate 校验错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultT<String> validHandler(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Iterator<FieldError> iterator = fieldErrors.iterator();
        FieldError error = iterator.next();
        sb.append(error.getField()).append(":").append(error.getDefaultMessage());
        while (iterator.hasNext()) {
            sb.append(";");
            sb.append(error.getField());
            sb.append(":");
            sb.append(error.getDefaultMessage());
        }
        return new ResultT<>(ResultT.FAIL_CODE, sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResultT<String> handler(Exception e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String message = StrTool.stringifyException(e);
        log.error("Rest请求:[{}]发生异常：{}", uri, message);
        return new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
    }

}
