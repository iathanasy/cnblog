package com.icss.cnblog.shiro;

import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysMenuEntity;
import com.icss.cnblog.entity.SysRoleEntity;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.service.SysRoleService;
import com.icss.cnblog.service.SysUserService;
import com.icss.cnblog.utils.MD5Util;
import com.icss.cnblog.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @description: shiro身份校验核心类
 * @author: Mr.Wang
 * @create: 2020-02-05 10:53
 **/
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMenuService menuService;

    /**
     * 认证(登录时调用)
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        log.debug("————身份认证方法————");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        SysUserEntity sysUser = userService.selectByUserName(username);
        //密码进行加密处理
        String pass = username + password;
        String passDES = MD5Util.encrypt(pass);
        if(sysUser == null || !passDES.equalsIgnoreCase(sysUser.getPassword())){
            throw new AccountException("帐号或密码不正确！");
        }
        if(CommonConst.USER_STATUS.equals(sysUser.getStatus())){
            throw new DisabledAccountException("此帐号已停用！");
        }
        //登录成功
        return new SimpleAuthenticationInfo(sysUser, password, getName());
    }

    /**
     * 授权(验证权限时调用)
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("————权限认证————");
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null){
            return info;
        }
        Long userId = sysUser.getUserId();

        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 权限列表
        Set<String> permissionSet = new HashSet<>();

        // 管理员拥有所有权限
        if(CommonConst.USER_ADMIN.equals(sysUser.getUserName())){
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        }else{
            // 赋予角色
            List<SysRoleEntity> roleList = roleService.selectRoleListByUserId(userId);
            for (SysRoleEntity role: roleList) {
                roles.add(role.getRoleKey());
            }
            //赋予权限
            List<SysMenuEntity> menuList = menuService.selectMenuListByUserId(userId);
            if (!CollectionUtils.isEmpty(menuList)) {

                for (SysMenuEntity menu : menuList) {
                    String permission = menu.getPerms();
                    if (StringUtils.isNotEmpty(permission)) {
                        permissionSet.addAll(Arrays.asList(permission.trim().split(",")));
                    }
                }
            }
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);
            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(permissionSet);
        }

        return info;
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
