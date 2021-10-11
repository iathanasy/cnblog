package com.icss.cnblog.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-25 10:12:48
 */
@Data
@TableName("sys_log")
public class SysLogEntity extends BaseEntlty {

	@TableId
	private Long logId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户操作
	 */
	private String operation;
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * 请求参数
	 */
	private String params;
	/**
	 * 执行时长(毫秒)
	 */
	private Long time;
	/**
	 * IP地址
	 */
	private String ip;
	/**
	 * 地址
	 */
	private String addr;
	/**
	 * 操作状态（0正常 1异常）
	 */
	private Integer status;

	/**
	 * 请求URL
	 */
	private String requestUrl;
	/**
	 * 请求方式
	 */
	private String requestMethod;
	/**
	 * 响应数据
	 */
	private String jsonResult;
	/**
	 * 错误消息
	 */
	private String errorMsg;
}
