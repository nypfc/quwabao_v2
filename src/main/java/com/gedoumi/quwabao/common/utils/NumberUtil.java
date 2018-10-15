package com.gedoumi.quwabao.common.utils;

import java.util.Random;

public class NumberUtil {

    public static Integer randomInt(int mix, int max){
        Random random = new Random();
        return random.nextInt(max-mix) + mix ;
    }

}
