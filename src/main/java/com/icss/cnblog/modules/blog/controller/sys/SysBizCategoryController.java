package com.icss.cnblog.modules.blog.controller.sys;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;
import com.icss.cnblog.modules.blog.service.BizCategoryService;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 类别表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@RestController
@RequestMapping("sys/category")
public class SysBizCategoryController {
    @Autowired
    private BizCategoryService categoryService;


    /**
     * 类别列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @RequiresPermissions("sys:category:list")
    public Result list(Query query) {
        List<BizCategoryEntity> list = categoryService.selectAllList();
        for (BizCategoryEntity c: list) {
            BizCategoryEntity entity = categoryService.selectById(c.getParentId());
            if(entity != null){
                c.setParentName(entity.getName());
            }
        }

        return Result.success(list);
    }

    /**
     * 类别选择(类别添加修改)
     * @param query
     * @return
     */
    @RequestMapping("select")
    @RequiresPermissions("sys:category:select")
    public Result select(Query query) {
        List<BizCategoryEntity> childs = categoryService.selectList();
       /* //添加顶级菜单
        BizCategoryEntity root = new BizCategoryEntity();
        root.setId(0L);
        root.setName("顶级类别");
        root.setParentId(-1L);
        root.setChilds(childs);
        List list = new ArrayList();
        list.add(root);*/
        return Result.success(childs);
    }

    /**
     * 类别选择(文章添加修改)
     * @return
     */
    @RequestMapping("tree")
    @RequiresPermissions("sys:category:tree")
    public Result tree() {
        List<BizCategoryEntity> list = categoryService.selectList();
        return Result.success(list);
    }

    /**
     * 添加类别
     * @param category
     * @return
     */
    @SysLog("添加类别")
    @PostMapping("add")
    @RequiresPermissions("sys:category:add")
    public Result add(@RequestBody BizCategoryEntity category){
        categoryService.insert(category);
        return Result.success();
    }

    /**
     * 信息
     */
    @RequestMapping("info/{categoryId}")
    @RequiresPermissions("sys:category:info")
    public Result info(@PathVariable("categoryId") Long categoryId){
        BizCategoryEntity category = categoryService.selectById(categoryId);

        if(null != category){
            if(!category.getParentId().equals(0L)){
                BizCategoryEntity s = categoryService.selectById(category.getParentId());
                category.setParentName(s.getName());
            }else{
                category.setParentName("顶级菜单");
            }
        }
        return Result.success(category);
    }

    @SysLog("修改类别")
    @PostMapping("update")
    @RequiresPermissions("sys:category:update")
    public Result update(@RequestBody BizCategoryEntity category){
        categoryService.updateById(category);
        return Result.success();
    }

    /**
     * 删除
     */
    @SysLog("删除类别")
    @PostMapping("delete")
    @RequiresPermissions("sys:category:delete")
    public Result delete(@RequestParam Long categoryId){
        List<BizCategoryEntity> list = categoryService.selectListParentId(categoryId);
        if(list.size() > 0){
            return Result.error("请先删除子类别");
        }
        //删除
        categoryService.delete(categoryId);
        return Result.success();
    }
}
