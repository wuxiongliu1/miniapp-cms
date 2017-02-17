package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateUser(@PathVariable(value = "id") Long id, String oldPassword, String newPassword, HttpServletRequest request) throws UnsupportedEncodingException {
        ApiResult apiResult = userService.updatePassword(id, oldPassword, newPassword);
        if (apiResult.getResultCode() == ResultCode.SUCCESS.getResultCode()) {
            // 更新session
            Object userObj = request.getSession().getAttribute("user");
            if (userObj != null) {
                User user = (User) userObj;
                user.setPassword(DigestUtils.md5Hex(newPassword.getBytes("utf-8")));
                request.getSession().setAttribute("user", user);
            }
        }

        return apiResult;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String userEdit(@PathVariable(value = "id") Long id, Model model) throws Exception {
        User user = userService.findOne(id);
        if (user == null) {
            throw new Exception(ResultCode.NO_USER.getResultMsg());
        }
        return "operateUser";
    }
}
