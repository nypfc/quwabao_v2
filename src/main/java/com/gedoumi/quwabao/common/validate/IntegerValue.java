package com.gedoumi.quwabao.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Integer类型参数验证注解
 *
 * @author Minced
 */
@Documented
@Constraint(validatedBy = {IntegerValueValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface IntegerValue {

    int[] value();

    String message() default "参数值不匹配";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
