package com.gedoumi.quwabao.common.validate;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * String参数验证类
 *
 * @author Minced
 */
public class StringValueValidator implements ConstraintValidator<StringValue, String> {

    private String[] value;

    @Override
    public void initialize(StringValue stringValue) {
        this.value = stringValue.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || this.value.length == 0)
            return true;
        return CollectionUtils.arrayToList(this.value).contains(value);
    }

}
