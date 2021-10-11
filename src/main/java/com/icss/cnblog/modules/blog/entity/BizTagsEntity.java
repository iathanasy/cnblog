package com.icss.cnblog.modules.blog.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.icss.cnblog.entity.BaseEntlty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@Data
@TableName("biz_tags")
public class BizTagsEntity extends BaseEntlty {

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 背景
	 */
	private String color;
}
