/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 *
 */

package com.huobanplus.miniapp.web.resolver;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by wuxiongliu on 2016/5/10.
 */
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserAuthenticationPrincipal.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String attributeName = parameter.getParameterAnnotation(UserAuthenticationPrincipal.class).value();
        if (StringUtils.isEmpty(attributeName)) {
            attributeName = parameter.getParameterName();
        }
        return webRequest.getAttribute(attributeName, RequestAttributes.SCOPE_REQUEST);
    }
}
