package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysMenuEntity;
import com.icss.cnblog.exception.GlobalException;
import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.shiro.session.service.ShiroService;
import com.icss.cnblog.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-03 16:34
 **/
@RestController
@RequestMapping("sys/menu")
public class SysMenuController extends BaseController{
    @Autowired
    private SysMenuService menuService;

    /**
     * 后台导航菜单
     * @return
     */
    @RequestMapping("nav")
    @RequiresPermissions("sys:menu:nav")
    public Result nav() {
        List<SysMenuEntity> nav;
        if(CommonConst.USER_ADMIN.equals(getUserName())){
            nav = menuService.selectNavMenus(null);
        }else{
            nav = menuService.selectNavMenus(getUserId());
        }
        return Result.success(nav);
    }

    /**
     * 菜单树
     * @return
     */
    @RequestMapping("tree")
    @RequiresPermissions("sys:menu:tree")
    public Result tree() {
        List<SysMenuEntity> tree = menuService.tree();
        return Result.success(tree);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public Result select(){
        //查询列表数据
        List<SysMenuEntity> menuList = menuService.tree();

        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId(0L);
        root.setMenuName("顶级菜单");
        root.setParentId(-1L);
        root.setChildMenus(menuList);
        List list = new ArrayList();
        list.add(root);
        return Result.success(list);
    }

    /**
     * 所有菜单列表
     */
    @RequestMapping("list")
    @RequiresPermissions("sys:menu:list")
    public Result list(){
        List<SysMenuEntity> menuList = menuService.selectAllMenuList();
        //
        for(SysMenuEntity sysMenuEntity : menuList){
            SysMenuEntity parentMenuEntity = menuService.selectById(sysMenuEntity.getParentId());
            if(parentMenuEntity != null){
                sysMenuEntity.setParentName(parentMenuEntity.getMenuName());
            }
        }
        return Result.success(menuList);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @SysLog("添加菜单")
    @PostMapping("add")
    @RequiresPermissions("sys:menu:add")
    public Result add(@RequestBody SysMenuEntity menu){
        //数据校验
        verifyForm(menu);
        ShiroService.clearCachedAuthorizationInfo();
        menuService.insert(menu);
        return Result.success();
    }

    /**
     * 菜单信息
     */
    @RequestMapping("info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public Result info(@PathVariable("menuId") Long menuId){
        SysMenuEntity menu = menuService.selectById(menuId);
        if(null != menu){
            if(0L != menu.getParentId()){
                SysMenuEntity s = menuService.selectById(menu.getParentId());
                menu.setParentName(s.getMenuName());
            }else{
                menu.setParentName("顶级菜单");
            }

        }
        return Result.success(menu);
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @SysLog("修改菜单")
    @PostMapping("update")
    @RequiresPermissions("sys:menu:update")
    public Result update(@RequestBody SysMenuEntity menu){
        //数据校验
        verifyForm(menu);
        ShiroService.clearCachedAuthorizationInfo();
        menuService.updateById(menu);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @SysLog("删除菜单")
    @PostMapping("delete")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestParam Long menuId){
        //判断是否有子菜单或按钮
        List<SysMenuEntity> menuList = menuService.selectListParentId(menuId);
        if(menuList.size() > 0){
            return Result.error("请先删除子菜单或按钮");
        }
        ShiroService.clearCachedAuthorizationInfo();
        menuService.delete(menuId);
        return Result.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu){
        if(StringUtils.isBlank(menu.getMenuName())){
            throw new GlobalException("菜单名称不能为空");
        }

        if(menu.getParentId() == null){
            throw new GlobalException("上级菜单不能为空");
        }

        //菜单
        if(menu.getMenuType() == CommonConst.MenuType.MENU.getValue()){
            if(StringUtils.isBlank(menu.getUrl())){
                throw new GlobalException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        String parentType = CommonConst.MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            SysMenuEntity parentMenu = menuService.selectById(menu.getParentId());
            parentType = parentMenu.getMenuType();
        }

        //目录、菜单
        if(menu.getMenuType() == CommonConst.MenuType.CATALOG.getValue() ||
                menu.getMenuType() == CommonConst.MenuType.MENU.getValue()){
            if(parentType != CommonConst.MenuType.CATALOG.getValue()){
                throw new GlobalException("上级菜单只能为目录类型");
            }
            return ;
        }

        //按钮
        if(menu.getMenuType() == CommonConst.MenuType.BUTTON.getValue()){
            if(parentType != CommonConst.MenuType.MENU.getValue()){
                throw new GlobalException("上级菜单只能为菜单类型");
            }
            return ;
        }
    }
}

