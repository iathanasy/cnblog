package com.icss.cnblog.shiro.session.service;

import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.shiro.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: Shiro-权限相关的业务处理
 * @author: Mr.Wang
 * @create: 2020-02-13 21:10
 **/
public class ShiroService {

    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout()
    {
        getSubject().logout();
    }

    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }

    public static SysUserEntity getSysUser()
    {
        SysUserEntity user = (SysUserEntity) getSubject().getPrincipal();
        if(null != user) {
            return user;
        }
        return null;
    }

    public static void setSysUser(SysUserEntity user)
    {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    /**
     * 清理缓存权限
     */
    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        ShiroRealm realm = (ShiroRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }
}
