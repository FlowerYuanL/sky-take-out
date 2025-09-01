package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoFill {

    /*操作类型*/
    OperationType value();
}
