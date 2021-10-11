package com.icss.cnblog.annotation;

import java.lang.annotation.*;

/**
 * @description: 系统日志注解
 * @author: Mr.Wang
 * @create: 2020-02-25 10:36
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";

    /**
     * 是否保存请求的参数
     */
    boolean paramsSave() default true;

}
