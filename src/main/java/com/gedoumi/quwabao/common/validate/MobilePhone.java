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
 * 手机号格式验证注解
 *
 * @author Minced
 */
@Documented
@Constraint(validatedBy = {MobilePhoneValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface MobilePhone {

    String message() default "wrong phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
