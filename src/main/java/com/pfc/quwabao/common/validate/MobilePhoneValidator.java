package com.pfc.quwabao.common.validate;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号格式验证器
 *
 * @author Minced
 */
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    private static final String REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(phone))
            return true;
        return Pattern.matches(REGEX, phone);
    }

}
