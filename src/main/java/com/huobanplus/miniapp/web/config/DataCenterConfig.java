package com.huobanplus.miniapp.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by wuxiongliu on 2017-02-07.
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.huobanplus.miniapp.web.repository"},
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@ImportResource({"classpath:miniapp_config_prod.xml", "classpath:miniapp_config_test.xml"})
public class DataCenterConfig {
}
