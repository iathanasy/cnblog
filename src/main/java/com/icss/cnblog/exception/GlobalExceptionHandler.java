package com.icss.cnblog.exception;

import com.icss.cnblog.utils.Result;
import com.icss.cnblog.utils.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-04 13:50
 **/
@Slf4j
//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Shiro权限认证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {UnauthorizedException.class})
    public Result unauthorizedExceptionHandle(Throwable e) {
        e.printStackTrace(); // 打印异常栈
        return Result.error(ResultEnum.UNAUTHZ);
    }

    /**
     * MethodArgumentTypeMismatchException ： 方法参数类型异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public Object methodArgumentTypeMismatchException(Throwable e) {
        log.error("url参数异常，请检查参数类型是否匹配！", e);
         return Result.error(ResultEnum.NVALID_PARAMS);

    }

    @ExceptionHandler(value = Exception.class)
    public Object handle(Throwable e,  HttpServletRequest req) {
        Result result = Result.error(e.getMessage());
        //业务异常
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            if(ex.getResultEnum() == null){
                log.error(ex.getMessage());
                result = Result.error(ex.getMessage());
            }else {
                result = Result.error(ex.getResultEnum());
            }
        }
        if(e instanceof DuplicateKeyException) {
            String msg = "数据库中已存在该记录";
            log.error(msg);
            result = Result.error(ResultEnum.BAD_REQUEST);
        }
        if(e instanceof NoHandlerFoundException) {
            String msg = "接口 [" + req.getRequestURI() + "] 不存在";
            log.error(msg);
            result = Result.error(ResultEnum.NOT_FOUND);
        }
        if(e instanceof HttpRequestMethodNotSupportedException) {
            //接口访问方式错误
            String msg = "接口 [" + req.getRequestURI() + "], "+ e.getMessage();
            log.error(msg);
            result = Result.error(ResultEnum.METHOD_NOT_ALLOWD);
        }

        if (isAjax(req)) {
            e.printStackTrace(); // 打印异常栈
            return result;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("msg", e.getMessage());
            modelAndView.addObject("url", req.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.setViewName("error");
            return modelAndView;
        }
    }

    public boolean isAjax(HttpServletRequest req){
        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        String contentTypeHeader = req.getHeader("Content-Type");
        String acceptHeader = req.getHeader("Accept");
        String xRequestedWith = req.getHeader("X-Requested-With");
        return (contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
    }
}
