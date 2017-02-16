package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wuxiongliu on 2017-02-16.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 更新用户的密码
     *
     * @param id
     * @param password
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateUser(@PathVariable(value = "id") Long id, String password) {
        return userService.updatePassword(id, password);
    }
}
