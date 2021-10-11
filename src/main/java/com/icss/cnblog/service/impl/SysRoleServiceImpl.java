package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysRoleEntity;
import com.icss.cnblog.mapper.SysRoleMapper;
import com.icss.cnblog.service.SysRoleMenuService;
import com.icss.cnblog.service.SysRoleService;
import com.icss.cnblog.service.SysUserRoleService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public PageUtils queryPage(Query query) {
        String keywords = query.getKeywords();
        Page<SysRoleEntity> page = this.selectPage(
                query.getPagePlus(),
                new EntityWrapper<SysRoleEntity>()
                        .like(StringUtils.isNotBlank(keywords),"role_name", keywords)
                        .or().like(StringUtils.isNotBlank(keywords),"role_key", keywords)
                        .orderBy("role_sort")
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysRoleEntity> selectRoleListByUserId(Long userId) {
        return baseMapper.selectRoleListByUserId(userId);
    }

    @Override
    public List<SysRoleEntity> selectRoleList() {
        return baseMapper.selectRoleList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleEntity role) {
        this.insert(role);

        //保存角色与菜单关系
        roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);

        //保存角色与菜单关系
        roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.deleteBatchIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        roleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        userRoleService.deleteBatch(roleIds);
    }

}
