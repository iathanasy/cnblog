package com.icss.cnblog.shiro.session;

import com.icss.cnblog.entity.SysUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 当前在线会话
 * @author: Mr.Wang
 * @create: 2020-02-13 14:28
 **/
@Data
public class UserOnline extends SysUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // session id
    private String sessionId;
    // 用户主机地址
    private String host;
    /** 登录地点 */
    private String loginLocation;
    /** 浏览器类型 */
    private String browser;
    /** 操作系统 */
    private String os;
    // 状态 0当前 1其它
    private String userStatus;
    // session创建时间
    private Date startTimestamp;
    // session最后访问时间
    private Date lastAccessTime;
    // 超时时间
    private Long timeout;
    //session 是否踢出
    private boolean sessionStatus = Boolean.TRUE;

    public UserOnline(){
    }

    public UserOnline(SysUserEntity user){
        super(user);
    }

}
