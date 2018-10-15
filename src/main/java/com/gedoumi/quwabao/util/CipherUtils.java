package com.gedoumi.quwabao.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * 加密解密工具
 */
public class CipherUtils {
    static Logger logger = LoggerFactory.getLogger(CipherUtils.class);

    private static final String PRIVATE_KEY_PATH = "pri";
    private static final String PUBLIC_KEY_PATH = "pub";

    /**
     * 根据私钥生成签名
     * @param inputString
     * @return
     */
    public static String generateSign(String inputString){
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        String sign = "";
        try {
            cipher = Cipher.getInstance("RSA");
            privKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY_PATH);

            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            cipherText = cipher.doFinal(inputString.getBytes());
            //加密后的东西
//            logger.info("cipher: {}" , new String(cipherText));
            sign = new String(Base64.getEncoder().encode(cipherText));
            logger.info("sign: {}" ,sign);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sign;
    }

    public static boolean verify(String sign, String inputString){
        RSAPublicKey pubKey;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY_PATH);

            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            //开始解密
            byte[] orginText = Base64.getDecoder().decode(sign);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            String verifyString = new String(cipher.doFinal(orginText));

            logger.info("verifyString: {}" , verifyString);
            if(StringUtils.equals(verifyString,inputString)){
                return true;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }


    /**
     * 获取私钥
     * @param fileName
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String fileName)throws Exception {
        File f = ResourceUtils.getFile("classpath:" + fileName);
        byte[] fileBytes = FileUtils.readFileToByteArray(f);
        byte[] keyBytes = Base64.getDecoder().decode(fileBytes);
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * 获取公钥
     * @param fileName
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String fileName) throws Exception {
        File f = ResourceUtils.getFile("classpath:" + fileName);
        byte[] fileBytes = FileUtils.readFileToByteArray(f);
        byte[] keyBytes = Base64.getDecoder().decode(fileBytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }


    public static String decry_RC4(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    public static String decry_RC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static byte[] encry_RC4_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes();
        return RC4Base(b_data, key);
    }

    public static String encry_RC4_string(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encry_RC4_byte(data, key)));
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
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

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    /**
     * 生成8位邀请码
     * @return
     */
    public static String generateCode(){
        Random random = new Random();
        String key = String.format("%04d",random.nextInt(1000));
        return encry_RC4_string(key, UUID.randomUUID().toString());
    }

    /**
     * 生成4位验证码
     * @return
     */
    public static String generateValidateCode(){
        Random random = new Random();
        String key = String.format("%02d",random.nextInt(10));
        return encry_RC4_string(key, UUID.randomUUID().toString()).toUpperCase();
    }



    public static void main(String[] args) {
//        Map<String,String> m = new HashMap<>();
//        for (int i=0;i<20000;i++){
//            String tmp = generateCode();
//            m.put(tmp, tmp);
//            System.out.println(tmp);
//        }
//        System.out.println(m.size());

    }

}
