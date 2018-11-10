package com.pfc.quwabao.common.validate;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Integer类型参数验证类
 *
 * @author Minced
 */
public class IntegerValueValidator implements ConstraintValidator<IntegerValue, Integer> {

    private int[] value;

    @Override
    public void initialize(IntegerValue integerValue) {
        this.value = integerValue.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null || this.value.length == 0)
            return true;
        return CollectionUtils.arrayToList(this.value).contains(value);
    }

}
