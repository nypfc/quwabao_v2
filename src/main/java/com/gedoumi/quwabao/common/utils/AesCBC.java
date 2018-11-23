package com.gedoumi.quwabao.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * AES加密
 * 对原始数据进行AES加密后，进行Base64编码转化
 */
public final class AesCBC {

    /*
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static Charset CHARSET = Charset.forName("UTF-8");

    /**
     * 私有化工具类构造方法
     */
    private AesCBC() {
    }

    /**
     * AES加密
     *
     * @param sSrc 加密字符串
     * @return 加密后的字符串
     * @throws Exception ex
     */
    public static String encrypt(String sSrc, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        byte[] raw = Base64.decodeBase64(secretKey.getBytes());

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        byte[] ivp = new byte[16];
        System.arraycopy(raw, 0, ivp, 0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivp);  // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] padding = pKCS7Encode(sSrc.getBytes(CHARSET).length);
        byte[] unEncrypted = ArrayUtils.addAll(sSrc.getBytes(CHARSET), padding);
        byte[] encrypted = cipher.doFinal(unEncrypted);
        return DigestUtils.sha1Hex(encrypted);
    }

    /**
     * 获得对明文进行补位填充的字节.
     *
     * @param count 需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    private static byte[] pKCS7Encode(int count) {
        // 计算需要填充的位数
        int BLOCK_SIZE = 32;
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        StringBuilder tmp = new StringBuilder();
        for (int index = 0; index < amountToPad; index++) {
            tmp.append(padChr);
        }
        return tmp.toString().getBytes(CHARSET);
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     *
     * @param a 需要转化的数字
     * @return 转化得到的字符
     */
    private static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}