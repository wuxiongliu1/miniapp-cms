package com.huobanplus.miniapp.web.inteceptor;

import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.service.UserService;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wuxiongliu on 2017-02-10.
 */

@Component
public class RequestInteceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User userSession = (User) request.getSession().getAttribute("user");
        if (StringUtil.isEmpty(userSession)) {
            response.sendRedirect("/toLogin");
            return false;
        }
        User user = userService.findUser(userSession.getUsername(), userSession.getPassword());
        if (user != null) {
            request.setAttribute("user", user);
            return true;
        } else {
            response.sendRedirect("/toLogin");
            return false;
        }
    }
}
