package com.icss.cnblog.config;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.icss.cnblog.handler.MetaHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * MybatisPlus 配置
 */
@Component
@MapperScan({"com.icss.cnblog.mapper","com.icss.cnblog.modules.blog.mapper"})
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public GlobalConfiguration globalConfig() {
        // MP 全局配置，更多内容进入类看注释
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        //配置公共字段自动填写
        globalConfig.setMetaObjectHandler(new MetaHandler());
        return globalConfig;
    }
}
