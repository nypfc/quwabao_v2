package com.gedoumi.quwabao.common.utils;

import lombok.extern.slf4j.Slf4j;

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

}
