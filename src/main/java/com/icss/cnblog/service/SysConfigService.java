package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysConfigEntity;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

import java.util.Map;

/**
 * 系统配置表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-25 10:12:48
 */
public interface SysConfigService extends IService<SysConfigEntity> {

    PageUtils queryPage(Query query);

    void website(Map<String, String> configs);

    Map<String, String> selectAll();
}

