package com.icss.cnblog.config;

import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * @description: 加载启动配置
 * @author: Mr.Wang
 * @create: 2020-04-19 16:23
 **/
@Configuration
public class StartupConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SysConfigService configService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadWebSite();
    }

    /**
     * 加载站点配置信息
     */
    private void loadWebSite(){
        CommonConst.WEBSITE = configService.selectAll();
    }
}
