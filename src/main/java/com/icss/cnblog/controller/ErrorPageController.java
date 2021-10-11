package com.icss.cnblog.controller;

import com.icss.cnblog.utils.Result;
import com.icss.cnblog.utils.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 错误页面
 */
@Slf4j
@Controller
public class ErrorPageController extends BasicErrorController {

    private final static String ERROR_PATH = "error/";

    public ErrorPageController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
     * 无权限
     * @return
     **/
    @RequestMapping({ERROR_PATH + "403.html",ERROR_PATH +"403"})
    public String unauth(){
        return "error/403";
    }

    /**
     * 找不到页面
     * @return
     **/
    @RequestMapping({ERROR_PATH +"404.html",ERROR_PATH +"404"})
    public String notfound(){
        return "error/404";
    }

     /**
     * 定义500的ModelAndView
     * @return
     **/
    @RequestMapping({ERROR_PATH +"500.html",ERROR_PATH +"500"})
    public ModelAndView errorHtml500(HttpServletRequest request, HttpServletResponse response){
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request,isIncludeStackTrace(request, MediaType.TEXT_HTML));
        model.put("msg","自定义错误信息");
        return new ModelAndView("error/500", model);
    }

    /**
     * 定义500的错误JSON信息
     * @param request
     * @return
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity error500(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        Result result;
        Integer code = Integer.parseInt(body.get("status")+"");
        String msg = (String) body.get("message");
        //未授权
        if(ResultEnum.UNAUTHZ.getCode() == code){
            result = Result.error(ResultEnum.UNAUTHZ);
        }else{
            result = Result.error(code, msg);
        }
        return new ResponseEntity(result, status);
    }

}
