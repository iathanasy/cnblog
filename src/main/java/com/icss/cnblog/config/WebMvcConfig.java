package com.icss.cnblog.config;

import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.interceptor.IndexInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-03 19:50
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private IndexInterceptor indexInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(CommonConst.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + CommonConst.profile + "/");

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");;
    }

    /**
     * 加载网站配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(indexInterceptor);
    }

    /**
     * 跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600 * 24);
    }


    private final static String ERROR_PATH = "/error";
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> containerCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                ErrorPage errorPage400 = new ErrorPage(HttpStatus.FORBIDDEN,ERROR_PATH+"/403.html");
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,ERROR_PATH +"/404.html");
                ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,ERROR_PATH+"/500.html");
                factory.addErrorPages(errorPage400,errorPage404,errorPage500);
            }
        };
    }
}
