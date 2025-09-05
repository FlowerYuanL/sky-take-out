package com.sky.aop;

import com.sky.annotation.LogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Pointcut("@annotation(com.sky.annotation.LogAnnotation)")
    private void LogJoinPoint() {}

    @Around("LogJoinPoint()")
    public Object generateLogs(ProceedingJoinPoint joinPoint) throws Throwable {
        //记录程序开始的时间
        Long startTime = System.currentTimeMillis();
        //获取方法签名——获取类名和方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        //获取传入的参数——可能为空
        List<?> args = CollectionUtils.arrayToList(joinPoint.getArgs());
        //获取签名上面的描述信息
        String value = method.getAnnotation(LogAnnotation.class).value();

        //输出日志
        log.info("[AOP日志] ==> 执行信息[{}]: {}.{}",value,className, methodName);
        if(!CollectionUtils.isEmpty(args)){
            log.info("[AOP日志] ==> 传入参数: {}", args);
        }

        //执行程序
        Object result = joinPoint.proceed();

        //记录程序结束的时间
        Long endTime = System.currentTimeMillis();
        //计算程序运行时间
        Long costTime = endTime - startTime;
        log.info("[AOP日志] <== 执行信息[{}]: {}.{} 执行完毕, 返回结果: {}",value, className, methodName, result);
        log.info("[AOP日志] <== 耗时: {} ms", costTime);
        return result;
    }
}
