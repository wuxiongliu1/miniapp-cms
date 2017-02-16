package com.huobanplus.miniapp.web.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wuxiongliu on 2017-02-13.
 */
public class CmsExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView model = new ModelAndView();
        model.setViewName("error");
        model.addObject("errorMsg", ex.getMessage());
        return model;
    }
}
