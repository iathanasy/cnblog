package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.entity.SysRoleEntity;
import com.icss.cnblog.service.SysRoleMenuService;
import com.icss.cnblog.service.SysRoleService;
import com.icss.cnblog.shiro.session.service.ShiroService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-08 17:24
 **/
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends BaseController{
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    /**
     * 角色列表
     */
    @RequestMapping("list")
    @RequiresPermissions("sys:role:list")
    public Result list(Query query){
        PageUtils page = roleService.queryPage(query);
        return Result.success(page);
    }

    /**
     * 角色选择
     */
    @RequestMapping("select")
    @RequiresPermissions("sys:role:select")
    public Result select(){
        List<SysRoleEntity> list = roleService.selectRoleList();
        return Result.success(list);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @SysLog("添加角色")
    @PostMapping("add")
    @RequiresPermissions("sys:role:add")
    public Result add(@RequestBody SysRoleEntity role){
        ShiroService.clearCachedAuthorizationInfo();
        roleService.add(role);
        return Result.success();
    }

    /**
     * 角色信息
     */
    @RequestMapping("info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public Result info(@PathVariable("roleId") Long roleId){
        SysRoleEntity role = roleService.selectById(roleId);
        //查询角色对应的菜单按钮
        List<Long> menuIdList = roleMenuService.queryMenuIdButtonList(roleId);
        role.setMenuIdList(menuIdList);
        return Result.success(role);
    }

    @SysLog("修改角色")
    @PostMapping("update")
    @RequiresPermissions("sys:role:update")
    public Result update(@RequestBody SysRoleEntity role){
        ShiroService.clearCachedAuthorizationInfo();
        roleService.update(role);
        return Result.success();
    }

    /**
     * 删除
     */
    @SysLog("删除角色")
    @PostMapping("delete")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Long[] roleIds){
        ShiroService.clearCachedAuthorizationInfo();
        roleService.deleteBatch(roleIds);
        return Result.success();
    }

}
