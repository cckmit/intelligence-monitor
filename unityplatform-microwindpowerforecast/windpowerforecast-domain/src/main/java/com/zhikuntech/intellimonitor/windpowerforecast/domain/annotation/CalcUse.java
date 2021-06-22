package com.zhikuntech.intellimonitor.windpowerforecast.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参与计算的属性注解
 *
 * @author liukai
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CalcUse {

    String value() default "";
}
