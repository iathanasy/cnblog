package com.icss.cnblog.controller;

import com.icss.cnblog.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 页面渲染
 * @author: Mr.Wang
 * @create: 2020-02-03 19:29
 **/
@Controller
public class RenderController{

    @RequestMapping("sys/{module}/{url}.html")
    public String module(@PathVariable("module") String module, @PathVariable("url") String url){
        return "sys/" + module + "/" + url;
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping({"login.html","login"})
    public String login(){
        return "login";
    }

    /**
     * 后台首页
     * @return
     */
    @RequestMapping({"sys/index.html", "sys/index"})
    public String sysHome(ModelMap map){
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        map.addAttribute("user", user);
        return "sys/index";
    }

    /**
     * 后台默认页
     * @return
     */
    @RequestMapping("sys/default.html")
    public String sysDefault(){
        return "sys/default";
    }

}
