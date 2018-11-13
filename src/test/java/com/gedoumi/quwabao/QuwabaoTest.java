package com.gedoumi.quwabao;

import com.gedoumi.quwabao.common.utils.CodeUtils;
import org.junit.Test;

public class QuwabaoTest {

    @Test
    public void test1() {
        String s = CodeUtils.generateCode();
        System.out.println(s);
    }

}
