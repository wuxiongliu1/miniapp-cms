package com.huobanplus.miniapp.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by wuxiongliu on 2017-02-10.
 */

@ActiveProfiles("test")
@Configuration
@ComponentScan({
        "com.huobanplus.miniapp.web.service"
})
@Import({DataCenterConfig.class})
public class MiniAppTestConfig {
}
