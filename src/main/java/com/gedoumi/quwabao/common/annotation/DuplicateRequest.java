package com.gedoumi.quwabao.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 防止重复提交的注解
 *
 * @author Minced
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface DuplicateRequest {
}
