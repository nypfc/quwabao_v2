package com.gedoumi.quwabao.common.constants;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.math.BigDecimal;

/**
 * 常量类
 */
public interface Constants {

    String USER_KEY = "sysUser";
    String API_USER_KEY = "user";
    String LOCK = "AtomicInteger";
    String V_CODE_KEY = "vCode";
    String INDEX_PATH = "/index";
    String DEVICE_ERROR_PATH = "/deviceError";
    String VALIDATE_ERROR_PATH = "/validateError";
    int EXPIRE_TIMES = 80;//60s
    BigDecimal PROFIT_RATE = new BigDecimal("0.05");
    BigDecimal FEE = new BigDecimal("0.005");
    BigDecimal TWO = new BigDecimal("2");
    int SCALE = 5;
    int SCALE_PROFIT = 2;
    BigDecimal FROZEN_RATE = new BigDecimal("0.5");
    BigDecimal FROZEN_DAYS = new BigDecimal("90");
    BigDecimal RENT_DAYS = new BigDecimal("30");
    BigDecimal REWARD_DAYS = new BigDecimal("30");
    BigDecimal GAP_LOCKPERDAY = new BigDecimal("0.0001");
    String RENT_NAME = "矿机";
    String ASSET_NAME = "pfc";
    int API_SUCCESS_CODE = 0;
    int SMS_DAY_COUNT = 10;
    String DEVICE_ID = "deviceId";
    String AUTH_TOKEN = "auth-token";
    String USER_PREFIX = "挖宝客";

}
