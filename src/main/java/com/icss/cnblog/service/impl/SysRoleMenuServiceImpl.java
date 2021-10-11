package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysRoleMenuEntity;
import com.icss.cnblog.mapper.SysRoleMenuMapper;
import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements SysRoleMenuService {

    @Autowired
    private SysMenuService menuService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});

        if(menuIdList.size() == 0){
            return ;
        }

        //保存角色与菜单关系
        List<SysRoleMenuEntity> list = new ArrayList<>(menuIdList.size());
        for(Long menuId : menuIdList){
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);

            list.add(sysRoleMenuEntity);
        }
        this.insertBatch(list);
    }

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    @Override
    public HashSet<String> queryMenuNameList(Long userId) {
        return baseMapper.queryMenuNameList(userId);
    }

    @Override
    public List<Long> queryMenuIdButtonList(Long roleId) {
        //无子菜单的ID
        List<Long> notChildIds = menuService.selectNotChildMenusId(menuService.list());
        //角色拥有的权限
        List<Long> ids = baseMapper.queryMenuIdButtonList(roleId);
        ids.retainAll(notChildIds);
        return ids;
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
