package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {

    Success(0, "0000", "成功"),
    Fail(1, "1111", "失败"),
    FailedToGetToken(2, "6665", "未获取到令牌"),
    UnLogin(2, "6666", "令牌无效"),
    RepeatLogin(2, "6667", "重复登录"),
    SysError(3, "8888", "系统异常"),
    UnknowError(4, "9999", "未知异常"),
    UserLoginError(5, "2222", "用户不存在或密码错误"),
    LoginTimesError(6, "2223", "请去【忘记密码】重置密码"),
    UserLocked(7, "2224", "用户锁定，请联系客服处理"),
    ValidateCodeExpire(8, "2225", "验证码错误或已经过期"),
    MobileExist(9, "2226", "手机号已经存在"),
    MobileError(10, "2227", "手机号不正确"),
    NameError(11, "2228", "用户名已经存在"),
    ValidateCodeError(12, "2229", "验证码错误"),
    InviteCodeError(13, "2230", "邀请码错误"),
    InviteCodeBindError(14, "2231", "邀请码已经绑定，不能再次使用"),
    RemainAssetError(15, "2232", "余额不足"),
    RentError(16, "2233", "只能租用一台矿机"),
    UserTeamError(17, "2234", "只能添加一次团队长信息"),
    UnFrozenError(18, "2235", "业绩没有达到最多要求"),
    UnFrozenTwiceError(19, "2236", "同一种的业绩只能解冻一次"),
    ApplyTeamRewardError(20, "2237", "不符合申请团队奖励的条件"),
    ApplyTeamRewardTwiceError(21, "2238", "同类型的团队奖励已经申请过"),
    TransMoneyError(22, "2239", "账户余额不足"),
    TransPswdError(23, "2240", "交易密码不正确"),
    TransMobileError(24, "2241", "转账手机号码不正确"),
    OrgPswdError(25, "2242", "原密码不正确"),
    ResetPswdError(26, "2243", "请输入密码"),
    NoNameError(27, "2244", "请输入用户名"),
    BindInviteCodeError(28, "2245", "不能循环绑定邀请码"),
    BindInviteSelfError(29, "2246", "不能绑定自己"),
    TransSelfError(30, "2247", "不能给自己转账"),
    TransChainError(31, "2248", "不能给团队之外的用户转账"),
    TeamBindError(32, "2249", "团队长不能绑定邀请码"),
    RentMaxError(33, "2250", "该类型矿机已经不再提供，请租用其他类型矿机"),
    DeviceError(34, "2251", "您的账号已经在其他设备登录"),
    GateWayError(35, "8889", "排队较多，请稍后查看【交易记录】"),
    WithDrawAmountEmty(36, "2252", "金额不能为空或者负数"),
    WithDrawAmountError(37, "2253", "余额不足"),
    WithDrawTimesError(38, "2254", "上次提现请求还未处理"),
    WithDrawSingleLimitError(39, "2255", "提现超过单笔限额"),
    WithDrawDayLimitError(40, "2256", "提现超过单日限额"),
    ValidateError(41, "2257", "未经过实名认证，不能进行交易"),
    ValidateAlready(42, "2258", "已经经过实名认证"),
    ValidateIdCardAlready(43, "2259", "身份证已经被使用"),
    ValidateIdParamError(44, "2260", "请输入实名认证信息"),
    SmsCountError(45, "2261", "验证码数量，已达今日上限"),
    UserTeamAddError(46, "2262", "已经注册的用户不能成为团队长"),
    TransMoneyNegError(47, "2263", "转账不能为负数"),
    AddRentTimeError(48, "2264", "每日23:00-00:00为结算期,期间暂停矿机租用服务"),
    WithDrawAddressError(49, "2265", "提现地址不能为空"),
    WithDrawError(50, "2267", "提现失败、请检查提现地址"),
    NotBetting(51, "2266", "非投注期"),
    Gaming(52, "2267", "游戏期"),
    Bouns(53, "2268", "算奖期"),
    ParamError(54, "2269", "参数错误");

    private int value;

    private String code;

    private String message;

    CodeEnum(int value, String code, String message) {
        this.value = value;
        this.code = code;
        this.message = message;
    }

}
