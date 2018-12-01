package com.gedoumi.quwabao.common.constants;

/**
 * API URL常量
 *
 * @author Minced
 */
public interface ApiConstants {

    /**
     * 版本号
     */
    String VERSION = "/v2";

    /**
     * APP用户
     */
    String APP_USER = VERSION + "/user";

    /**
     * APP短信
     */
    String APP_SMS = VERSION + "/sms";

    /**
     * APP竞猜
     */
    String APP_GUESS = VERSION + "/guess";

    /**
     * APP登录
     */
    String APP_LOGIN = VERSION + "/login";

    /**
     * 系统矿机
     */
    String SYS_RENT = VERSION + "/rent";

    /**
     * APP交易
     */
    String APP_TRANSACTION = VERSION + "/trans";

    /**
     * APP充值回调
     */
    String APP_RECHARGE_CALLBACK = "/api/v2/pfc/recharge";

}
