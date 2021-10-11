package com.icss.cnblog.interceptor;

import com.icss.cnblog.consts.CommonConst;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 前台拦截器
 * @author: Mr.Wang
 * @create: 2020-04-19 16:42
 **/
@Component
public class IndexInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String requestType = request.getHeader("X-Requested-With");
        //非ajax请求
        if(!"XMLHttpRequest".equals(requestType)){
            // 站点设置
            request.setAttribute("website", CommonConst.WEBSITE);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
