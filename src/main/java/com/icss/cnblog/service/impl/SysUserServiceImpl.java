package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.exception.GlobalException;
import com.icss.cnblog.mapper.SysUserMapper;
import com.icss.cnblog.service.SysUserRoleService;
import com.icss.cnblog.service.SysUserService;
import com.icss.cnblog.utils.Assert;
import com.icss.cnblog.utils.MD5Util;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-03 16:33
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService  {

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public SysUserEntity selectByUserName(String userName) {
        return baseMapper.selectByUserName(userName);
    }

    @Override
    public PageUtils selectPageList(Query query) {
        Page page = query.getPagePlus();
        page.setRecords(baseMapper.selectPageList(query, page));
        //Page<SysUserEntity> page = this.selectPage(query.getPagePlus(), new EntityWrapper<>());
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserEntity user) {

        SysUserEntity userEntity = selectByUserName(user.getUserName());
        Assert.isNotNull(userEntity, "用户名已存在！");

        //密码进行加密处理
        String passDES = MD5Util.encrypt(user.getUserName() + user.getPassword());
        user.setPassword(passDES);

        this.insert(user);

        //保存用户与角色关系
        userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserEntity user) {
        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }else{
            //密码进行加密处理
            String passDES = MD5Util.encrypt(user.getUserName() + user.getPassword());
            user.setPassword(passDES);
        }
        this.updateById(user);

        //保存用户与角色关系
        userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] userIds) {
        //删除用户
        this.deleteBatchIds(Arrays.asList(userIds));
        //删除用户与角色关系
        userRoleService.deleteBatchByUserId(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String userName, String oldPass, String newPass) {
        SysUserEntity user = selectByUserName(userName);
        Assert.isNull(user, "用户名不存在");
        //旧密码加密
        String oldPassHex = MD5Util.encrypt(user.getUserName() + oldPass);
        //密码错误
        if(!user.getPassword().equals(oldPassHex)){
            throw new GlobalException("旧密码错误");
        }

        //新密码加密
        String newPassHex = MD5Util.encrypt(user.getUserName() + newPass);
        user.setPassword(newPassHex);
        this.updateById(user);
    }
}
