package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.config.MiniAppTestBase;
import com.huobanplus.miniapp.web.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
public class UserServiceTest extends MiniAppTestBase {

    @Autowired
    private UserService userService;

    private String mockUsername = UUID.randomUUID().toString();
    private String mockPassword = "12345";

    @Test
    public void test() {
        Assert.assertNotNull(userService);
    }

    @Test
    public void testAddUser() {
        ApiResult apiResult = userService.addUser(mockUsername, mockPassword);
        User user = (User) apiResult.getData();
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testFindUser() {
        userService.addUser(mockUsername, mockPassword);

        User user = userService.findUser(mockUsername, mockPassword);
        Assert.assertNotNull(user);
        Assert.assertEquals(mockUsername, user.getUsername());
        Assert.assertEquals(mockPassword, user.getPassword());
    }


}
