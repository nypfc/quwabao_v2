package com.gedoumi.quwabao;

import com.gedoumi.quwabao.util.MD5EncryptUtil;
import org.junit.Test;

public class QuwabaoTest {

    @Test
    public void md5Test() {
        String phone = "13810060370";
        String pwd = "111111";

        System.out.println(MD5EncryptUtil.md5Encrypy(pwd));
        System.out.println(MD5EncryptUtil.md5Encrypy(pwd, phone));
    }

}
