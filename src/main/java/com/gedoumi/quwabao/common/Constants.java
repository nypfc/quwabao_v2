package com.gedoumi.quwabao.common;

import java.math.BigDecimal;

public class Constants {

    public final static String USER_KEY = "sysUser";
    public final static String API_USER_KEY = "user";
    public final static String LOCK = "AtomicInteger";
    public final static String V_CODE_KEY = "vCode";
    public final static String INDEX_PATH = "/index";
    public final static String DEVICE_ERROR_PATH = "/deviceError";
    public final static String VALIDATE_ERROR_PATH = "/validateError";
    public final static int EXPIRE_TIMES = 80;//60s
    public final static BigDecimal PROFIT_RATE = new BigDecimal("0.05");
    public final static BigDecimal FEE = new BigDecimal("0.005");
    public final static BigDecimal TWO = new BigDecimal("2");
    public final static int SCALE = 5;
    public final static int SCALE_PROFIT = 2;
    public final static BigDecimal FROZEN_RATE = new BigDecimal("0.5");
    public final static BigDecimal FROZEN_DAYS = new BigDecimal("90");
    public final static BigDecimal RENT_DAYS = new BigDecimal("30");
    public final static BigDecimal REWARD_DAYS = new BigDecimal("30");
    public final static BigDecimal GAP_LOCKPERDAY = new BigDecimal("0.0001");
    public final static String RENT_NAME = "矿机";
    public final static String ASSET_NAME = "pfc";
    public final static int API_SUCCESS_CODE = 0;
    public final static int SMS_DAY_COUNT = 10;
}
