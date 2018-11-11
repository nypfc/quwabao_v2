package com.gedoumi.quwabao.common.utils;

import java.util.Random;

/**
 * Code生成工具类
 *
 * @author Minced
 */
public final class CodeUtils {

    private static final String str[] = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "a", "b", "c", "d", "e", "f"
    };

    private static final Random random = new Random();

    private CodeUtils() {
    }

    /**
     * 生成8位邀请码
     *
     * @return 8位验证码
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(str.length);
            sb.append(str[index]);
        }
        return sb.toString();
    }

    /**
     * 生成6位短信验证码
     *
     * @return 短信验证码
     */
    public static String generateSMSCode() {
        return String.valueOf(random.nextInt(999999));
    }

    /**
     * 生成4位验证码
     *
     * @return 4位验证码
     */
    public static String generateValidateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(str.length);
            sb.append(str[index]);
        }
        return sb.toString().toUpperCase();
    }

}
