package com.huobanplus.miniapp.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by wuxiongliu on 2017-02-07.
 */

@Configuration
@ComponentScan
@Import({DataCenterConfig.class})
public class RootConfig {
}
