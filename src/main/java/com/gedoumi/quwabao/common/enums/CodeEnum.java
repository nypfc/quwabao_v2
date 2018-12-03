package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {

    SysError(-1, "系统错误"),
    Success(0, "请求成功"),
    Fail(1111, "请求失败"),
    EmptyToken(6665, "令牌不能为空"),
    InvalidToken(6666, "无效的令牌"),
    UnknowError(9999, "未知异常"),
    PasswordError(2222, "密码错误"),
    TooManyError(2223, "错误次数超过3次，需要重置密码"),
    UserLocked(2224, "账号已被锁定"),
    SmsCodeError(2225, "短信验证码错误或已经过期"),
    MobileExist(2226, "手机号已经存在"),
    MobileNotExist(2227, "手机号不存在"),
    NameError(2228, "用户名已经存在"),
    ValidateCodeError(2229, "验证码错误"),
    InviteCodeError(2230, "邀请码错误"),
    InviteCodeBindError(2231, "邀请码已经绑定，不能再次使用"),
    RemainAssetError(2232, "资产不足"),
    UserTeamError(2234, "只能添加一次团队长信息"),
    UnFrozenError(2235, "业绩没有达到最多要求"),
    UnFrozenTwiceError(2236, "同一种的业绩只能解冻一次"),
    ApplyTeamRewardError(2237, "不符合申请团队奖励的条件"),
    ApplyTeamRewardTwiceError(2238, "同类型的团队奖励已经申请过"),
    TransMoneyError(2239, "账户余额不足"),
    PayPswdError(2240, "交易密码不正确"),
    TransMobileNotExist(2241, "转账手机号码不存在"),
    OrgPswdError(2242, "原密码不正确"),
    ResetPswdError(2243, "请输入密码"),
    NoNameError(2244, "请输入用户名"),
    BindInviteCodeError(2245, "一个用户只能拥有一个上级"),
    TransSelfError(2247, "不能给自己转账"),
    TransChainError(2248, "不能给团队之外的用户转账"),
    TeamBindError(2249, "团队长不能绑定邀请码"),
    RentMaxError(2250, "该类型矿机已经不再提供，请租用其他类型矿机"),
    DeviceError(2251, "您的账号已经在其他设备登录"),
    GateWayError(8889, "排队较多，请稍后查看【交易记录】"),
    DuplicateRequest(8890, "上次请求还未处理完成"),
    WithDrawAmountEmty(2252, "金额不能为空或者负数"),
    WithDrawAmountError(2253, "余额不足"),
    WithDrawTimesError(2254, "上次提现请求还未处理"),
    WithDrawSingleLimitError(2255, "提现单笔限额有误"),
    WithDrawDayLimitError(2256, "提现超过单日限额"),
    SmsCountError(2261, "短信已达当日上限"),
    UserTeamAddError(2262, "已经注册的用户不能成为团队长"),
    TransMoneyNegError(2263, "转账不能为负数"),
    AddRentTimeError(2264, "23:00-00:00不能租用矿机"),
    WithDrawAddressError(2265, "提现地址不能为空"),
    WithDrawError(2267, "提现失败、请检查提现地址"),
    NotBetting(2266, "非投注期"),
    Gaming(2267, "游戏期"),
    Bouns(2268, "算奖期"),
    ParamError(2269, "参数错误"),
    PayPswdNull(2270, "还未设置支付密码"),
    ZoneError(2271, "区号错误"),
    SendSMSError(2272, "发送短息错误"),
    RepeatedlySMS(2273, "不能重复发送短信"),
    MobileSame(2274, "修改手机号不能与原手机号相同"),
    ;

    private Integer code;

    private String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
