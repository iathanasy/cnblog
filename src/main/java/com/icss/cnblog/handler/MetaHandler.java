package com.icss.cnblog.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.icss.cnblog.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:  mybatisplus自定义填充公共字段 ,即没有传的字段自动填充
 * @author: Mr.Wang
 * @create: 2020-02-09 15:34
 **/
@Slf4j
@Component
public class MetaHandler extends MetaObjectHandler{

    /**
     * 新增填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //获取当前登录用户
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        setFieldValByName("createBy", user.getUserName(),metaObject);
        setFieldValByName("createTime", new Date(),metaObject);
        setFieldValByName("updateBy", user.getUserName(),metaObject);
        setFieldValByName("updateTime", new Date(),metaObject);

    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //获取当前登录用户
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        /*metaObject.setValue("updateBy", user.getUserName());
        metaObject.setValue("updateTime", new Date());*/
        setFieldValByName("updateBy", user.getUserName(),metaObject);
        setFieldValByName("updateTime", new Date(),metaObject);
    }
}
