package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysUserRoleEntity;
import com.icss.cnblog.mapper.SysUserRoleMapper;
import com.icss.cnblog.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRoleEntity> implements SysUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        HashMap<String, Object> map = new HashMap();
        map.put("user_id", userId);
        //先删除用户与角色关系
        this.deleteByMap(map);

        if(roleIdList == null || roleIdList.size() == 0){
            return ;
        }

        //保存用户与角色关系
        List<SysUserRoleEntity> list = new ArrayList<>(roleIdList.size());
        for(Long roleId : roleIdList){
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);

            list.add(sysUserRoleEntity);
        }
        this.insertBatch(list);
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    @Override
    public HashSet<String> queryRoleNameList(Long userId) {
        return baseMapper.queryRoleNameList(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Long[] roleIds){
        return baseMapper.deleteBatch(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatchByUserId(Long[] userIds) {
        return baseMapper.deleteBatchByUserId(userIds);
    }
}
