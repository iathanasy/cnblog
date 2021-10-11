package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

/**
 * 用户信息表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
public interface SysUserService extends IService<SysUserEntity> {

    public SysUserEntity selectByUserName(String userName);

    public PageUtils selectPageList(Query query);

    public void add(SysUserEntity user);

    void update(SysUserEntity user);

    void deleteBatch(Long[] userIds);

    /**
     * 修改密码
     * @param userName       用户名
     * @param oldPass     原密码
     * @param newPass  新密码
     */
    void updatePassword(String userName, String oldPass, String newPass);
}

