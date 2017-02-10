package com.huobanplus.miniapp.web.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthenticationPrincipal {
    String value() default "";
}