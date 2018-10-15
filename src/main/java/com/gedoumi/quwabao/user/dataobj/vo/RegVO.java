package com.gedoumi.quwabao.user.dataobj.vo;

import java.io.Serializable;

public class RegVO implements Serializable {


    private static final long serialVersionUID = 6928367277245599085L;

    private String mobile;

    private String validateCode;

    private String smsCode;

    private String password;

    private String regInviteCode;
    private String userName;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegInviteCode() {
        return regInviteCode;
    }

    public void setRegInviteCode(String regInviteCode) {
        this.regInviteCode = regInviteCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
