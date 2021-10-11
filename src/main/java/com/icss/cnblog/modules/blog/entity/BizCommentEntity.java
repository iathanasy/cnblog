package com.icss.cnblog.modules.blog.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.icss.cnblog.entity.BaseEntlty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-07-05 16:50:16
 */
@Data
@TableName("biz_comment")
public class BizCommentEntity  extends BaseEntlty {
	private static final long serialVersionUID = 1L;

	/**
	 * 评论ID
	 */
	@TableId
	private Long id;
	/**
	 * 父级评论的id,默认为0
	 */
	private Long parentId;
	/**
	 * 文章id
	 */
	private Long articleId;
	/**
	 * 评论人的ID
	 */
	private Long userId;
	/**
	 * 
	 */
	private String userName;
	/**
	 * 评论人的QQ
	 */
	private String qq;
	/**
	 * 是否审核通过 0-未审核 1-审核通过
	 */
	private Integer status;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论层级
	 */
	private String thread;
	/**
	 * 点赞数
	 */
	private Long likeNum;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * 评论时的地址
	 */
	private String address;
	/**
	 * 评论时的系统类型
	 */
	private String os;
	/**
	 * 评论时的系统的简称
	 */
	private String osShortName;
	/**
	 * 评论时的浏览器类型
	 */
	private String browser;
	/**
	 * 评论时的浏览器的简称
	 */
	private String browserShortName;
}
