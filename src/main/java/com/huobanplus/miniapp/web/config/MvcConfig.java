package com.huobanplus.miniapp.web.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huobanplus.miniapp.web.exception.CmsExceptionHandler;
import com.huobanplus.miniapp.web.inteceptor.RequestInteceptor;
import com.huobanplus.miniapp.web.resolver.AuthArgumentResolver;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-07.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.huobanplus.miniapp.web.controller",
        "com.huobanplus.miniapp.web.service",
        "com.huobanplus.miniapp.web.entity.support",
        "com.huobanplus.miniapp.web.inteceptor"
})
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestInteceptor requestInteceptor;

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createGsonHttpMessageConverter());
    }

    /**
     * 配置转换器，json
     *
     * @return
     */
    private GsonHttpMessageConverter createGsonHttpMessageConverter() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
//                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(StringUtil.TIME_PATTERN)
                .create();
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson);
        return gsonConverter;
    }

    /**
     * 配置静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    /**
     * 取得当前登录用户的信息注解
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new AuthArgumentResolver());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 开启默认转发
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInteceptor).addPathPatterns("/**").excludePathPatterns("/login", "/logout", "/open/**", "/ueditor/**");
    }

    /**
     * 配置thymeleaf 模板引擎
     *
     * @return
     */
    @Bean
    public ViewResolver thymeleafViewResovler() {
        ServletContextTemplateResolver rootTemplateResolver = new ServletContextTemplateResolver();
        rootTemplateResolver.setPrefix("/views/");
        rootTemplateResolver.setSuffix(".html");
        rootTemplateResolver.setTemplateMode("HTML5");
        rootTemplateResolver.setCharacterEncoding("UTF-8");
        rootTemplateResolver.setCacheable(false);

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(rootTemplateResolver);


        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setOrder(1);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateEngine(engine);
        resolver.setCache(false);
        resolver.setContentType("text/html;charset=utf-8");
        return resolver;
    }

    /**
     * 异常统一处理bean
     *
     * @return
     */
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new CmsExceptionHandler();
    }
}
