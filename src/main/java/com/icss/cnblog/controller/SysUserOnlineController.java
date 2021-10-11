package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.shiro.session.service.SessionService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 在线用户
 * @author: Mr.Wang
 * @create: 2020-02-13 15:44
 **/
@RestController
@RequestMapping("sys/online")
public class SysUserOnlineController extends BaseController{

    @Autowired
    private SessionService sessionService;

    /**
     * 在线列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @RequiresPermissions("sys:online:list")
    public Result list(Query query) {
        PageUtils page = sessionService.selectPageList(query);
        return Result.success(page);
    }

    /**
     * 强制踢出用户
     * @param sessionId
     * @return
     */
    @SysLog("强踢用户")
    @RequestMapping("kickout")
    @RequiresPermissions("sys:online:kickout")
    public Result kickout(@RequestBody String sessionId) {
        sessionService.kickout(sessionId);
        return Result.success();
    }
}
