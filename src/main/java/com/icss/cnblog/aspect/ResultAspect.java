package com.icss.cnblog.aspect;

import com.icss.cnblog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description: 接口返回值获取
 * @author: Mr.Wang
 * @create: 2020-02-10 20:07
 **/
@Component
@Aspect
@Slf4j
public class ResultAspect {

    @Pointcut("execution(* com.icss.cnblog.*.*.*(..))")
    public void resultPointCut() {

    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(pointcut ="resultPointCut()", returning = "result")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object result) {
        if (result instanceof Result) {
            Result resultVO = (Result) result;
            String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
            String name = joinPoint.getSignature().getName();
            log.warn("[{}] {} {}",declaringTypeName,name,resultVO);
        }
    }
}
