package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysConfigEntity;
import com.icss.cnblog.service.SysConfigService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-25 10:32
 **/
@RestController
@RequestMapping("sys/config")
public class SysConfigController extends BaseController{
    @Autowired
    private SysConfigService configService;

    @RequestMapping("list")
    @RequiresPermissions("sys:config:list")
    public Result list(Query query){
        PageUtils page = configService.queryPage(query);
        return Result.success(page);
    }

    /**
     * 添加
     * @param config
     * @return
     */
    @SysLog("添加配置")
    @PostMapping("add")
    @RequiresPermissions("sys:config:add")
    public Result add(@RequestBody SysConfigEntity config){
        configService.insert(config);
        this.flushWebSite();
        return Result.success();
    }

    /**
     * 信息
     */
    @RequestMapping("info/{configId}")
    @RequiresPermissions("sys:config:info")
    public Result info(@PathVariable("configId") Long configId){
        SysConfigEntity config = configService.selectById(configId);
        return Result.success(config);
    }

    @SysLog("修改配置")
    @PostMapping("update")
    @RequiresPermissions("sys:config:update")
    public Result update(@RequestBody SysConfigEntity config){
        configService.updateById(config);
        this.flushWebSite();
        return Result.success();
    }

    /**
     * 删除
     */
    @SysLog("删除配置")
    @PostMapping("delete")
    @RequiresPermissions("sys:config:delete")
    public Result delete(@RequestBody Long[] configIds){
        configService.deleteBatchIds(Arrays.asList(configIds));
        this.flushWebSite();
        return Result.success();
    }

    /**
     * 系统站点配置
     * @param configs
     * @return
     */
    @SysLog("网站配置")
    @PostMapping("website")
    @RequiresPermissions("sys:config:website")
    public Result website(@RequestParam Map<String, String> configs){
        configService.website(configs);
        this.flushWebSite();
        return Result.success();
    }

    /**
     * 刷新站点信息
     */
    private void flushWebSite(){
        CommonConst.WEBSITE.clear();
        CommonConst.WEBSITE = configService.selectAll();
    }
}
