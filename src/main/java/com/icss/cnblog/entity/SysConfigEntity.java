package com.icss.cnblog.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 系统配置表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-25 10:12:48
 */
@Data
@TableName("sys_config")
public class SysConfigEntity extends BaseEntlty {

	@TableId
	private Long configId;
	/**
	 * 配置关键字
	 */
	private String configKey;
	/**
	 * 配置项内容
	 */
	private String configValue;
}
