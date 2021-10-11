package com.icss.cnblog.controller;

import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.service.SysRoleMenuService;
import com.icss.cnblog.service.SysUserRoleService;
import com.icss.cnblog.service.SysUserService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import com.icss.cnblog.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-03 16:34
 **/
@Slf4j
@RestController
@RequestMapping("sys/user")
public class SysUserController extends BaseController{
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @RequestMapping("list")
    @RequiresPermissions("sys:user:list")
    public Result list(Query query) {
        PageUtils page = userService.selectPageList(query);
        return Result.success(page);
    }

    /**
     * 详细信息
     */
    @PostMapping("/detailed")
    @RequiresPermissions("sys:user:detailed")
    public Result detailed(){
        SysUserEntity user = userService.selectById(getUserId());
        user.setPassword("");//密码置空
        //用户角色
        HashSet<String> roles = userRoleService.queryRoleNameList(getUserId());
        //角色菜单
        HashSet<String> menus = roleMenuService.queryMenuNameList(getUserId());

        Map map = new HashMap();
        map.put("user", user);
        map.put("roles", roles);
        map.put("menus", menus);
        return Result.success(map);
    }

    @SysLog("修改用户信息")
    @PostMapping("detailed/update")
    @RequiresPermissions("sys:user:detailed:update")
    public Result updateDetailed(@RequestBody SysUserEntity user){
        userService.updateById(user);
        return Result.success();
    }

    @SysLog("添加用户")
    @PostMapping("add")
    @RequiresPermissions("sys:user:add")
    public Result add(@RequestBody SysUserEntity user){
        userService.add(user);
        return Result.success();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public Result info(@PathVariable("userId") Long userId){
        SysUserEntity user = userService.selectById(userId);
        user.setPassword("");//密码置空
        //获取用户所属的角色列表
        List<Long> roleIdList = userRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return Result.success(user);
    }

    @SysLog("修改用户")
    @PostMapping("update")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody SysUserEntity user){
        userService.update(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Long[] userIds){

        if(ArrayUtils.contains(userIds, 1L)){
            return Result.error("超级用户不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return Result.error("当前用户不能删除");
        }
        userService.deleteBatch(userIds);
        return Result.success();
    }


    /**
     * 修改登录用户密码
     */
    @SysLog(value = "修改密码")
    @PostMapping("/password")
    @RequiresPermissions("sys:user:password")
    public Result password(String oldPassword, String newPassword){
        //更新密码
        userService.updatePassword(getUserName(), oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 上传头像
     */
    @SysLog(value = "上传头像",paramsSave = false)
    @PostMapping("/uploadAvatar")
    @RequiresPermissions("sys:user:upload:avatar")
    public Result uploadAvatar(@RequestParam("avatarfile") MultipartFile file){

        SysUserEntity currentUser = getUser();
        try
        {
            if (!file.isEmpty())
            {
                String avatar = UploadUtil.upload(CommonConst.getAvatarPath(), file);
                currentUser.setAvatar(avatar);

                if (userService.updateById(currentUser))
                {
                    setUser(userService.selectById(currentUser.getUserId()));
                    return Result.success(avatar);
                }
            }
            return Result.error("头像文件为空");
        }
        catch (Exception e)
        {
            log.error("修改头像失败！", e);
            return Result.error(e.getMessage());
        }

    }

}

