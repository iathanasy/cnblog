package com.icss.cnblog.controller;

import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.service.SysUserService;
import com.icss.cnblog.utils.RequestUtil;
import com.icss.cnblog.utils.Result;
import com.icss.cnblog.utils.kaptcha.Captcha;
import com.icss.cnblog.utils.kaptcha.GifCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-04 14:24
 **/
@Slf4j
@RestController
public class LoginController extends BaseController{

    @Autowired
     private SysUserService userService;

    /**
     * 获取验证码（Gif版本）
     * @param response
     */
    @RequestMapping(value="gifCode",method= RequestMethod.GET)
    public void getGifCode(HttpServletResponse response){
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146,40,4);
            //输出
            captcha.out(response.getOutputStream());

        } catch (Exception e) {
            log.error("获取验证码异常："+e.getMessage());
        }
    }


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("ajaxLogin")
    public Result login(String username, String password, String kaptcha){
        String vcode = RequestUtil.getSession(CommonConst.SESSION_KEY_KAPTCHA).toString();
        if(StringUtils.isEmpty(kaptcha) || !vcode.equalsIgnoreCase(kaptcha)){
            return Result.error("验证码错误");
        }
        //读取一次后把验证码清空，这样每次登录都必须获取验证码
        RequestUtil.removeSession(CommonConst.SESSION_KEY_KAPTCHA);

        SysUserEntity sysUser = userService.selectByUserName(username);
        if(sysUser == null){
            return Result.error("用户不存在");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password );
        SecurityUtils.getSubject().login(token);

        sysUser.setLoginDate(new Date());
        sysUser.setLoginIp(RequestUtil.getIp());
        userService.updateById(sysUser);

        return Result.success();
    }

    /**
     * 退出
     * @return
     */
    @RequestMapping(value="logout",method =RequestMethod.GET)
    public Result logout(){
        try {
            //退出
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Result.success();
    }
}
