package com.icss.cnblog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.utils.Query;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    public List<SysUserEntity> selectByExample(SysUserEntity user);

    @Select("select * from sys_user where user_name = #{userName}")
    public SysUserEntity selectByUserName(String userName);

    public List<SysUserEntity> selectPageList(Query query, Page page);
}