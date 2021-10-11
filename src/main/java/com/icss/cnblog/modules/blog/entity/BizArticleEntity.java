package com.icss.cnblog.modules.blog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.icss.cnblog.entity.BaseEntlty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文章表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:27
 */
@Data
@TableName("biz_article")
public class BizArticleEntity extends BaseEntlty{

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 文章类别Id
	 */
	private Long categoryId;
	/**
	 * 文章标题
	 */
	private String title;
	/**
	 * 文章摘要（简介）
	 */
	private String summary;
	/**
	 * 文章封面图片
	 */
	private String coverImage;
	/**
	 * 是否markdown编辑器 1是 0否
	 */
	private Integer md;
	/**
	 * 文章内容
	 */
	private String content;
	/**
	 * markdown版的文章内容
	 */
	private String contentMd;
	/**
	 * 状态 0草稿 1发布
	 */
	private Integer status;
	/**
	 * 是否置顶 0否 1是
	 */
	private Integer top;
	/**
	 * 是否推荐 0否 1是
	 */
	private Integer recommended;
	/**
	 * 是否原创
	 */
	private Integer original;
	/**
	 * 是否开启评论 0否 1是
	 */
	private Integer comment;
	/**
	 * 访问密码 为空表示没有密码
	 */
	private String password;
	/**
	 * 浏览量
	 */
	private Long pv;

	@TableField(exist=false)
	private String remark;

	/**
	 * 标签
	 */
	@TableField(exist=false)
	private HashSet<String> tags;
}
