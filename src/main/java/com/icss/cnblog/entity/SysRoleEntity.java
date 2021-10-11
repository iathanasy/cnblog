package com.icss.cnblog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.List;

/**
 * 角色信息表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
@Data
@TableName("sys_role")
public class SysRoleEntity extends BaseEntlty {
	/**
	 * 角色ID
	 */
	@TableId(value = "role_id", type = IdType.AUTO)
	private Long roleId;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 角色权限字符串
	 */
	private String roleKey;
	/**
	 * 显示顺序
	 */
	private Integer roleSort;
	/**
	 * 角色状态（0正常 1停用）
	 */
	private String status;

	@TableField(exist=false)
	private List<Long> menuIdList;

}
