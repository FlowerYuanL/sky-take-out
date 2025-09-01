package com.sky.aop;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AutoFillAop {

    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    private void getAutoFillPT(){}

    @Before("getAutoFillPT()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("[AutoFill日志] ==> 开始自动填充公共字段");
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法对象
        Method method = signature.getMethod();
        log.info("[AutoFill日志] ==> 捕获到的方法:{}.{}",method.getDeclaringClass().getName(),method.getName());
        //通过放射获取注解上的value值
        OperationType value = signature.getMethod().getAnnotation(AutoFill.class).value();
        log.info("[AutoFill日志] ==> 检测方法类型为:{}",value);
        //获取传递的参数,约定传递的第一个参数为需要进行修饰的参数
        Object[] args = joinPoint.getArgs();
        //判空
        if(args == null || args.length == 0){
            return;
        }
        //获取实体对象
        Object entity = args[0];
        String name = entity.getClass().getName();
        log.info("[AutoFill日志] <== 补全对象的类名为:{}",name);
        //准备需要赋值的对象
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();
        log.info("[AutoFill日志] <== 自动补全的参数为 操作者id:{},当前时间:{}",id,now);

        //判定操作类型
        if(value.equals(OperationType.INSERT)){
            //为4个公共字段进行赋值
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射为对象属性赋值
            setCreateTime.invoke(entity, now);
            setUpdateTime.invoke(entity, now);
            setCreateUser.invoke(entity,id);
            setUpdateUser.invoke(entity,id);
        }
        if(value.equals(OperationType.UPDATE)){
            //为2个公共字段赋值
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity,id);
        }
    }
}
