package com.icss.cnblog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 菜单权限表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
@ToString
@Data
@TableName("sys_menu")
public class SysMenuEntity extends BaseEntlty {
	/**
	 * 菜单ID
	 */
	@TableId(value = "menu_id", type = IdType.AUTO)
	private Long menuId;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 父菜单ID
	 */
	private Long parentId;
	/**
	 * 显示顺序
	 */
	private Integer orderNum;
	/**
	 * 路由地址
	 */
	private String url;
	/**
	 * 是否为外链（0是 1否）
	 */
	private Integer isFrame;
	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	private String menuType;
	/**
	 * 菜单状态（0显示 1隐藏）
	 */
	private String visible;
	/**
	 * 权限标识
	 */
	private String perms;
	/**
	 * 菜单图标
	 */
	private String icon;

	/////////////////非数据库字段///////////////////
	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * 子菜单
	 */
	@TableField(exist=false)
	private List<SysMenuEntity> childMenus;

}
