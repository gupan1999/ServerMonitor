package com.monitor.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLogParam {
    private final Logger logger = LoggerFactory.getLogger(MethodLogParam.class);

    @Pointcut("@annotation(com.monitor.utils.LogParam)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            logger.info("请求{}.{}入参日志：【{}】", joinPoint.getTarget().getClass(), methodName, joinPoint.getArgs());
        } catch (Exception e) {
            logger.error("入参日志打印异常", e);
        }
    }
}