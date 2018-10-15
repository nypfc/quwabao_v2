package com.gedoumi.quwabao.user.dataobj.vo;

import java.io.Serializable;

public class BindRegVO implements Serializable {


    private static final long serialVersionUID = 3042550618090146837L;
    private String regInviteCode;
    private String userName;

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
