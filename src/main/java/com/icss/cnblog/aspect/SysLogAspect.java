package com.icss.cnblog.aspect;

import com.alibaba.fastjson.JSONObject;
import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.entity.SysLogEntity;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.service.SysLogService;
import com.icss.cnblog.utils.AddressUtils;
import com.icss.cnblog.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.icss.cnblog.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time, null, result);

        return result;
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        saveSysLog(joinPoint, 0L ,e, null);
    }


    private void saveSysLog(JoinPoint joinPoint, long time, Exception e,  Object jsonResult) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            if(syslog.paramsSave()) {
                String params = JSONObject.toJSONString(args[0]);
                sysLog.setParams(StringUtils.substring(params, 0, 2000));
            }
        }catch (Exception ex){

        }

        //获取request
        HttpServletRequest request = RequestUtil.getRequest();
        //设置IP地址
        sysLog.setIp(AddressUtils.getIpAddr(request));
        //设置请求地址
        sysLog.setAddr(AddressUtils.getRealAddressByIP(sysLog.getIp()));
        // 设置请求方式
        sysLog.setRequestMethod(request.getMethod());
        // 设置请求路径
        sysLog.setRequestUrl(request.getRequestURI());

        //用户名
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUserName();
        sysLog.setUsername(username);

        //异常消息存储
        if (e != null)
        {
            sysLog.setStatus(1);
            sysLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
        }

        // 响应数据
        sysLog.setJsonResult(JSONObject.toJSONString(jsonResult));

        sysLog.setTime(time);
        //保存系统日志
        sysLogService.asyncSaveSysLog(sysLog);
    }
}
