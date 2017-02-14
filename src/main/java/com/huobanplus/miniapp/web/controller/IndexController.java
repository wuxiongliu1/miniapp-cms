package com.huobanplus.miniapp.web.controller;

import com.baidu.ueditor.ActionEnter;
import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request,Model model) {
        boolean remember = (boolean) request.getSession().getAttribute("remember");
        if(remember == true){

        }
        return "login";
    }

    @RequestMapping(value = {"/", "index"})
    public String index(@UserAuthenticationPrincipal(value = "user") User user) {
        return "index";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult login(HttpServletRequest request, String username, String password, Model model, boolean savedPassword) {
        User user = userService.findUser(username, password);
        if (user == null) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            return ApiResult.resultWith(ResultCode.NO_USER);
        }

        request.getSession().setAttribute("user", user);
        request.getSession().setMaxInactiveInterval(1200);// session过期时间20分钟

        return ApiResult.resultWith(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "login";
    }

    @RequestMapping(value = "/ueditor")
    public String ueditor(){
        return "ueditordemo";
    }

    @RequestMapping(value = "/ueditor/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding( "utf-8" );
        response.setHeader("Content-Type" , "text/html");

        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext application = webApplicationContext.getServletContext();
        String rootPath = application.getRealPath( "/" );

        response.getWriter().write( new ActionEnter( request, rootPath ).exec() );
    }
}
