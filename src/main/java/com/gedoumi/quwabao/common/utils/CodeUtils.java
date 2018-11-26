package com.gedoumi.quwabao.common.utils;

import org.springframework.lang.NonNull;

import java.util.Random;
import java.util.UUID;

/**
 * Code生成工具类
 *
 * @author Minced
 */
public final class CodeUtils {

    private static final Random random = new Random();

    private CodeUtils() {
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
     * 生成8位邀请码
     *
     * @return 8位验证码
     */
    public static String generateCode() {
        Random random = new Random();
        String key = String.format("%04d", random.nextInt(1000));
        return encry_RC4_string(key, UUID.randomUUID().toString());
    }

    private static String encry_RC4_string(@NonNull String data, @NonNull String key) {
        byte[] bytes = RC4Base(data.getBytes(), key);
        StringBuilder strbytes = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            strbytes.append((char) b);
        }
        String s = strbytes.toString();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str.append(s4);
        }
        return str.toString();  // 0x表示十六进制
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            assert key != null;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte[] state = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

}
