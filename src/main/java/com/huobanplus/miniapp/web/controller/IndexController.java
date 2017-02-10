package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = {"/", "index"})
    public String index(@UserAuthenticationPrincipal(value = "user") User user) {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, String username, String password, Model model, boolean savedPassword) {
        User user = userService.findUser(username, password);
        if (user == null) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "login";
        }

        request.getSession().setAttribute("user", user);
        request.getSession().setMaxInactiveInterval(1200);// session过期时间20分钟

        return "index";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "login";
    }
}
