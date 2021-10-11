package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysLogEntity;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

/**
 * 系统日志
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-25 10:12:48
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Query query);

    void asyncSaveSysLog(SysLogEntity sysLog);
}

