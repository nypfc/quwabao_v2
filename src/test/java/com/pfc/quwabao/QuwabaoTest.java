package com.pfc.quwabao;

import org.junit.Test;

import java.math.BigDecimal;

public class QuwabaoTest {

    @Test
    public void test1() {
        System.out.println(new BigDecimal(1000.10000).setScale(5, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
    }

}
