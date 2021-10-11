package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysLogEntity;
import com.icss.cnblog.mapper.SysLogMapper;
import com.icss.cnblog.service.SysLogService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Query query) {
        String keywords = query.getKeywords();
        Page<SysLogEntity> page = this.selectPage(
                query.getPagePlus(),
                new EntityWrapper<SysLogEntity>()
                        .like(StringUtils.isNotBlank(keywords),"username", keywords)
                        .orderBy("create_time", false)
        );

        return new PageUtils(page);
    }

    @Async
    @Override
    public void asyncSaveSysLog(SysLogEntity sysLog) {
        this.insert(sysLog);
    }
}
