package com.zhikuntech.intellimonitor.core.commons.golden.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 代志豪
 * 2021/6/9 10:44
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoldenId {

     int value() default 0;

     String scope() default "";

     /**
      * 实体属性注解。
      * 使用例子：com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO
      */
     boolean required() default false;
}
