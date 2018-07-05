package com.prim.router.primrouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    /**
     * 设置路由地址/main/text
     * @return 地址
     */
    String path();

    /**
     * 设置路由组，若为空以路由地址的第一个来作为组名main
     * @return 路由组
     */
    String group() default "";
}
