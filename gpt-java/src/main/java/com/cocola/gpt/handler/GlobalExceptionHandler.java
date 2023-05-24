package com.cocola.gpt.handler;


import com.alibaba.cola.dto.Response;
import com.cocola.gpt.exception.GptException;
import com.cocola.gpt.utils.WebFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * @Author: yangshiyuan
 * @Date: 2022/8/22
 * @Description: 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INTERNAL_SERVER_ERROR = "50001";

    @ExceptionHandler(GptException.class)
    public Response handler(HttpServletRequest request, GptException e) {
        LOG.error("gptException error|msg={}|url={}|", e.getMessage(), WebFrameworkUtils.getRequestUrl(request), e);
        return Response.buildFailure("" + e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handler(HttpServletRequest request, Exception e) {
        LOG.error("sysException error|msg={}|url={}|", e.getMessage(), WebFrameworkUtils.getRequestUrl(request), e);
        return Response.buildFailure(INTERNAL_SERVER_ERROR, "系统异常，请联系管理员");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response handler(HttpServletRequest request, ConstraintViolationException e) {
        LOG.error("sysException error|msg={}|url={}|", e.getMessage(), WebFrameworkUtils.getRequestUrl(request), e);
        return Response.buildFailure(INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 自定义验证异常
     *
     * @RequestBody上validate失败后抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        LOG.error("sysException error|msg={}|url={}|", e.getMessage(), WebFrameworkUtils.getRequestUrl(request), e);
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Response.buildFailure(INTERNAL_SERVER_ERROR, message);
    }

    /**
     * 自定义验证异常
     *
     * @RequestBody上valid失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Response validExceptionHandler(HttpServletRequest request, BindException e) {
        LOG.error("sysException error|msg={}|url={}|", e.getMessage(), WebFrameworkUtils.getRequestUrl(request), e);
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Response.buildFailure(INTERNAL_SERVER_ERROR, message);
    }
}
