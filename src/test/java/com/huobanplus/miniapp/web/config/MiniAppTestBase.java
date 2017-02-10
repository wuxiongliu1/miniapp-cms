package com.huobanplus.miniapp.web.config;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wuxiongliu on 2017-02-10.
 */

@ActiveProfiles("test")
@ContextConfiguration(classes = {MiniAppTestConfig.class})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class MiniAppTestBase {
}
