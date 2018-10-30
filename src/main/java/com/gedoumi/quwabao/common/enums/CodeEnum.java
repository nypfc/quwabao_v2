package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {

    SysError(-1, "System error"),
    Success(0, "Request success"),
    Fail(1111, "Request failure"),
    EmptyTokenOrDeviceId(6665, "Token or device id is empty"),
    InvalidToken(6666, "Invalid token"),
    RepeatLogin(6667, "Repeat login"),
    UnknowError(9999, "未知异常"),
    PasswordError(2222, "Password error"),
    TooManyPasswordMistakes(2223, "Too many password mistakes, Need reset password"),
    UserLocked(2224, "User Locked"),
    ValidateCodeExpire(2225, "验证码错误或已经过期"),
    MobileExist(2226, "Mobile already exists"),
    MobileNotExist(2227, "Mobile not exist"),
    NameError(2228, "用户名已经存在"),
    ValidateCodeError(2229, "验证码错误"),
    InviteCodeError(2230, "邀请码错误"),
    InviteCodeBindError(2231, "邀请码已经绑定，不能再次使用"),
    RemainAssetError(2232, " Not enough asset"),
    RentError(2233, "只能租用一台矿机"),
    UserTeamError(2234, "只能添加一次团队长信息"),
    UnFrozenError(2235, "业绩没有达到最多要求"),
    UnFrozenTwiceError(2236, "同一种的业绩只能解冻一次"),
    ApplyTeamRewardError(2237, "不符合申请团队奖励的条件"),
    ApplyTeamRewardTwiceError(2238, "同类型的团队奖励已经申请过"),
    TransMoneyError(2239, "账户余额不足"),
    TransPswdError(2240, "交易密码不正确"),
    TransMobileError(2241, "转账手机号码不正确"),
    OrgPswdError(2242, "原密码不正确"),
    ResetPswdError(2243, "请输入密码"),
    NoNameError(2244, "请输入用户名"),
    BindInviteCodeError(2245, "不能循环绑定邀请码"),
    BindInviteSelfError(2246, "不能绑定自己"),
    TransSelfError(2247, "不能给自己转账"),
    TransChainError(2248, "不能给团队之外的用户转账"),
    TeamBindError(2249, "团队长不能绑定邀请码"),
    RentMaxError(2250, "该类型矿机已经不再提供，请租用其他类型矿机"),
    DeviceError(2251, "您的账号已经在其他设备登录"),
    GateWayError(8889, "排队较多，请稍后查看【交易记录】"),
    WithDrawAmountEmty(2252, "金额不能为空或者负数"),
    WithDrawAmountError(2253, "余额不足"),
    WithDrawTimesError(2254, "上次提现请求还未处理"),
    WithDrawSingleLimitError(2255, "提现超过单笔限额"),
    WithDrawDayLimitError(2256, "提现超过单日限额"),
    ValidateError(2257, "未经过实名认证，不能进行交易"),
    ValidateAlready(2258, "已经经过实名认证"),
    ValidateIdCardAlready(2259, "身份证已经被使用"),
    ValidateIdParamError(2260, "请输入实名认证信息"),
    SmsCountError(2261, "SMS reach today's upper limit"),
    UserTeamAddError(2262, "已经注册的用户不能成为团队长"),
    TransMoneyNegError(2263, "转账不能为负数"),
    AddRentTimeError(2264, "23:00-00:00 out of rent service"),
    WithDrawAddressError(2265, "提现地址不能为空"),
    WithDrawError(2267, "提现失败、请检查提现地址"),
    NotBetting(2266, "非投注期"),
    Gaming(2267, "游戏期"),
    Bouns(2268, "算奖期"),
    ParamError(2269, "Parameters error"),
    SendSMSError(2270, "发送短息错误"),
    RepeatedlySMS(2271, "Send SMS repeatedly"),
    ;

    private Integer code;

    private String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
