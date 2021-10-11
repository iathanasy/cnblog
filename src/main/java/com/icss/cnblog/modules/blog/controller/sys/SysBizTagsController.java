package com.icss.cnblog.modules.blog.controller.sys;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import com.icss.cnblog.modules.blog.service.BizTagsService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 标签表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@RestController
@RequestMapping("sys/tag")
public class SysBizTagsController {
    @Autowired
    private BizTagsService tagsService;

    @RequestMapping("list")
    @RequiresPermissions("sys:tag:list")
    public Result list(Query query) {
        PageUtils page = tagsService.selectPageList(query);
        return Result.success(page);
    }


    /**
     * 文章标签选择
     * @return
     */
    @RequestMapping("select")
    @RequiresPermissions("sys:tag:select")
    public Result select() {
        List<BizTagsEntity> tags = tagsService.selectAll();
        return Result.success(tags);
    }


    /**
     * 添加标签
     * @param tag
     * @return
     */
    @SysLog("添加标签")
    @PostMapping("add")
    @RequiresPermissions("sys:tag:add")
    public Result add(@RequestBody BizTagsEntity tag){
        tagsService.insert(tag);
        return Result.success();
    }

    /**
     * 标签信息
     */
    @RequestMapping("info/{tagId}")
    @RequiresPermissions("sys:tag:info")
    public Result info(@PathVariable("tagId") Long tagId){
        BizTagsEntity tag = tagsService.selectById(tagId);
        return Result.success(tag);
    }

    @SysLog("修改标签")
    @PostMapping("update")
    @RequiresPermissions("sys:tag:update")
    public Result update(@RequestBody BizTagsEntity tag){
        tagsService.updateById(tag);
        return Result.success();
    }

    /**
     * 删除
     */
    @SysLog("删除标签")
    @PostMapping("delete")
    @RequiresPermissions("sys:tag:delete")
    public Result delete(@RequestBody Long[] tagIds){
        tagsService.deleteBatch(tagIds);
        return Result.success();
    }
}
