package com.icss.cnblog.controller;

import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.shiro.session.service.ShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-04 14:24
 **/
@RestController
@Slf4j
public class BaseController {

    protected SysUserEntity getUser(){
        return ShiroService.getSysUser();
    }

    protected void setUser(SysUserEntity user){
        ShiroService.setSysUser(user);
    }

    protected Long getUserId(){
        return getUser().getUserId();
    }

    protected String getUserName(){
        return getUser().getUserName();
    }
}
