package com.sdnc.common.annotation;

import org.beetl.sql.annotation.builder.Builder;
import org.beetl.sql.annotation.builder.FillStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在插入或者更新的时候,填充当前登录人ID的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Builder(UpdateUserConvert.class)
public @interface UpdateUser {
	FillStrategy value() default FillStrategy.INSERT_UPDATE;
}
