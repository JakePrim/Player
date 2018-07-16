package com.prim.router.primrouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/15 - 下午7:36
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Extra {
    String name() default "";
}
