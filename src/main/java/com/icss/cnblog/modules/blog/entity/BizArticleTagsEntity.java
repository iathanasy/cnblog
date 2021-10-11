package com.icss.cnblog.modules.blog.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章和标签关联表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@Data
@TableName("biz_article_tags")
public class BizArticleTagsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 文章ID
	 */
	private Long articleId;
	/**
	 * 标签Id
	 */
	private Long tagId;
}
