package com.gedoumi.quwabao.common.utils;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具类
 *
 * @author Minced
 */
public final class MD5EncryptUtil {

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 私有化工具类狗仔方法
     */
    private MD5EncryptUtil() {
    }

    /**
     * MD5加密
     *
     * @param str 带加密的字符串
     * @return 加密后的字符串
     */
    public static String md5Encrypy(String str) {
        return md5Encrypy(str, null);
    }

    /**
     * MD5加密（加盐）
     *
     * @param str  带加密的字符串
     * @param salt 盐值
     * @return 加密后的字符串
     */
    public static String md5Encrypy(String str, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (!StringUtils.isEmpty(salt)) {
                messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] digest = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            return new String(encode(digest));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new BusinessException(CodeEnum.SysError);
        }
    }

    /**
     * byte数组转换为char数组
     *
     * @param data byte数组
     * @return char数组
     */
    private static char[] encode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

}
