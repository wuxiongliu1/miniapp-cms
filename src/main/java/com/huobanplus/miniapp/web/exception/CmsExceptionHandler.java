package com.huobanplus.miniapp.web.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wuxiongliu on 2017-02-13.
 */
public class CmsExceptionHandler implements HandlerExceptionResolver {

    private static final Log log = LogFactory.getLog(CmsExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView model = new ModelAndView();
        model.setViewName("error");
        model.addObject("errorMsg", ex.getMessage());
        log.info("errorMsg: " + ex.getMessage());
        return model;
    }
}
