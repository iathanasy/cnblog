package com.icss.cnblog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户信息表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
@Data
@TableName("sys_user")
public class SysUserEntity extends BaseEntlty {

	/**
	 * 用户ID
	 */
	@TableId(value = "user_id", type = IdType.AUTO)
	private Long userId;
	/**
	 * 用户账号
	 */
	private String userName;
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 用户类型（00系统用户）
	 */
	private String userType;
	/**
	 * 用户邮箱
	 */
	private String email;
	/**
	 * 手机号码
	 */
	private String phonenumber;
	/**
	 * 用户性别（0男 1女 2未知）
	 */
	private String sex;
	/**
	 * 头像地址
	 */
	private String avatar;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 帐号状态（0正常 1停用）
	 */
	private String status;
	/**
	 * 最后登陆IP
	 */
	private String loginIp;
	/**
	 * 最后登陆时间
	 */
	private Date loginDate;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	public SysUserEntity(){}
	public SysUserEntity(SysUserEntity user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.nickName = user.getNickName();
		this.userType = user.getUserType();
		this.email = user.getEmail();
		this.phonenumber = user.getPhonenumber();
		this.sex = user.getSex();
		this.avatar = user.getAvatar();
		this.password = user.getPassword();
		this.status = user.getStatus();
		this.loginIp = user.getLoginIp();
		this.loginDate = user.getLoginDate();
	}
}
