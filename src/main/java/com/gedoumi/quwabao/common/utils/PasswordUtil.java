package com.gedoumi.quwabao.common.utils;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码工具类
 *
 * @author Minced
 */
@Slf4j
public final class PasswordUtil {

    /**
     * 密码加密
     *
     * @param mobile   手机号
     * @param password 带加密的密码密码
     * @return 加密后的密码
     */
    public static String passwordEncrypt(String mobile, String password) {
        return MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));
    }

    /**
     * 支付密码加密
     *
     * @param userId      用户ID
     * @param payPassword 支付密码
     * @return 加密后的密码
     */
    public static String payPasswordEncrypt(Long userId, String payPassword) {
        return MD5EncryptUtil.md5Encrypy(payPassword, String.valueOf(userId));
    }

    /**
     * 支付密码验证
     * 待验证的支付密码不需要加密传递，此方法会进行加密
     *
     * @param userId              用户ID
     * @param userPayPassword     用户的支付密码
     * @param validatePayPassword 待验证的支付密码
     */
    public static void payPasswordValidate(Long userId, String userPayPassword, String validatePayPassword) {
        if (StringUtils.isEmpty(userPayPassword)) {
            log.error("手机号:{}还未设置支付密码");
            throw new BusinessException(CodeEnum.PayPswdNull);
        }
        String encrypyPassword = MD5EncryptUtil.md5Encrypy(validatePayPassword, String.valueOf(userId));  // 密码加密
        if (!userPayPassword.equals(encrypyPassword)) {
            log.error("手机号:{} 支付密码错误", validatePayPassword);
            throw new BusinessException(CodeEnum.PayPswdError);
        }
    }

}
