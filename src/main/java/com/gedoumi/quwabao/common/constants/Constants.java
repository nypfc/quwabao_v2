package com.gedoumi.quwabao.common.constants;

import java.text.SimpleDateFormat;

/**
 * 常量类
 */
public interface Constants {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String API_USER_KEY = "user";
    Integer API_SUCCESS_CODE = 0;
    String AUTH_TOKEN = "auth-token";
    String USER_PREFIX = "挖宝客";

}
