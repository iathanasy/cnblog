package com.icss.cnblog.modules.blog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.icss.cnblog.entity.BaseEntlty;
import lombok.Data;

import java.util.List;

/**
 * 类别表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@Data
@TableName("biz_category")
public class BizCategoryEntity extends BaseEntlty{

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 父级ID
	 */
	private Long parentId;
	/**
	 * 类别名称
	 */
	private String name;
	/**
	 * 类别别名(用于url访问)
	 */
	private String alias;
	/**
	 * 状态 0不显示 1显示
	 */
	private Integer status;
	/**
	 * 是否显示导航栏 0否 1是
	 */
	private Integer nav;
	/**
	 * 显示顺序
	 */
	private Integer sort;
	/**
	 * 背景
	 */
	private String color;


	/**
	 * 父级名称
	 */
	@TableField(exist=false)
	private String parentName;
	/**
	 * 子集
	 */
	@TableField(exist=false)
	private List<BizCategoryEntity> childs;
}
