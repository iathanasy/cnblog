package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.service.SysLogService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-25 10:32
 **/
@RestController
@RequestMapping("sys/log")
public class SysLogController extends BaseController{
    @Autowired
    private SysLogService logService;

    @RequestMapping("list")
    @RequiresPermissions("sys:log:list")
    public Result list(Query query){
        PageUtils page = logService.queryPage(query);
        return Result.success(page);
    }

    /**
     * 删除
     */
    @SysLog("删除日志")
    @PostMapping("delete")
    @RequiresPermissions("sys:log:delete")
    public Result delete(@RequestBody Long[] logIds){
        logService.deleteBatchIds(Arrays.asList(logIds));
        return Result.success();
    }
}
