package com.gedoumi.quwabao;

import org.junit.Test;

import java.math.BigDecimal;

public class QuwabaoTest {

    @Test
    public void test1() {
        BigDecimal b = new BigDecimal("100.123");
        BigDecimal bigDecimal = new BigDecimal("10");
        System.out.println(bigDecimal);
        BigDecimal negate = bigDecimal.negate();
        System.out.println(negate);
        System.out.println(b.add(negate));
    }

}
