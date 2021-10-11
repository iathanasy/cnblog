package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysMenuEntity;
import com.icss.cnblog.mapper.SysMenuMapper;
import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;

    @Override
    public List<SysMenuEntity> list(){
        return queryMenuListParentId(selectAllMenuList());
    }

    @Override
    public List<SysMenuEntity> tree() {
        return queryMenuListParentId(selectAllMenuList());
    }

    @Override
    public List<SysMenuEntity> selectMenuListByUserId(Long userId) {
        return baseMapper.selectMenuListByUserId(userId);
    }

    @Override
    public List<SysMenuEntity> selectUrlAndPermsMenuList() {
        return baseMapper.selectUrlAndPermsMenuList();
    }

    @Override
    public List<SysMenuEntity> selectAllNotButtonMenuList(Long userId) {
        return baseMapper.selectAllNotButtonMenuList(userId);
    }

    @Override
    public List<SysMenuEntity> selectAllMenuList() {
        return baseMapper.selectAllMenuList();
    }

    @Override
    public List<SysMenuEntity> selectNavMenus(Long userId) {
        //用户菜单列表
        List<SysMenuEntity> menus;
        if(userId != null){
            menus = selectAllNotButtonMenuList(userId);
        }else{
            menus = this.selectList(new EntityWrapper<SysMenuEntity>()
                    .ne("menu_type",CommonConst.MenuType.BUTTON.getValue())
                    .and().eq("visible", 0));
        }
        return queryMenuListParentId(menus);
    }

    @Override
    public List<Long> selectNotChildMenusId(List<SysMenuEntity> meuns) {
        List<Long> ids = new ArrayList<Long>();
        for (SysMenuEntity menu: meuns) {
            if(menu.getChildMenus() == null){
                ids.add(menu.getMenuId());
            }else{
                ids.addAll(selectNotChildMenusId1(menu.getChildMenus()));
            }
        }
        return ids;
    }

    @Override
    public List<SysMenuEntity> selectListParentId(Long parentId) {
        HashMap map = new HashMap<String, Object>();
        map.put("parent_id", parentId);
        return this.selectByMap(map);
    }

    @Override
    public void delete(Long menuId) {
        //删除菜单
        this.deleteById(menuId);
        HashMap map = new HashMap<String, Object>();
        map.put("menu_id", menuId);
        //删除菜单与角色关联
        roleMenuService.deleteByMap(map);
    }

    private List<Long> selectNotChildMenusId1(List<SysMenuEntity> meuns) {
        List<Long> ids = new ArrayList<Long>();
        for (SysMenuEntity menu: meuns) {
            if(menu.getChildMenus() == null){
                ids.add(menu.getMenuId());
            }else{
                ids.addAll(selectNotChildMenusId1(menu.getChildMenus()));
            }
        }
        return ids;
    }


    /**
     * 查询所有一级菜单列表
     * @param menus
     * @return
     */
    private List<SysMenuEntity> queryMenuListParentId(List<SysMenuEntity> menus){
        List<SysMenuEntity> menuList = new ArrayList<SysMenuEntity>();
        // 先找到所有的一级菜单
        for (SysMenuEntity menu: menus) {
            // 一级菜单为0
            if( menu.getParentId().equals(0L)){
                menuList.add(menu);
            }
        }

        // for调用递归，循环获取所有子菜单
        for (SysMenuEntity menu: menus) {
            menu.setChildMenus(queryChildMenus(menu.getMenuId(), menus));
        }
        return menuList;
    }

    /**
     * 递归调用
     * @param parentId
     * @param menus
     * @return
     */
    private List<SysMenuEntity> queryChildMenus(Long parentId, List<SysMenuEntity> menus){
        // 子菜单
        List<SysMenuEntity> childMenus = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity menu: menus) {
            if(menu.getParentId().equals(parentId)){
                childMenus.add(menu);
            }
        }

        if (childMenus.size() == 0) {
            return null;
        }

        //递归调用添加子菜单
        for (SysMenuEntity menu: childMenus) {
            if(CommonConst.MenuType.CATALOG.getValue() == menu.getMenuType()){
                menu.setChildMenus(queryChildMenus(menu.getMenuId(), menus));
            }
        }

        return childMenus;
    }

}
