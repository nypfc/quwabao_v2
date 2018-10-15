package com.gedoumi.quwabao.common.base;

import java.io.Serializable;

public class LoginToken implements Serializable {


    private static final long serialVersionUID = 5198802158488954236L;

    private String mobilePhone;

    private String userName;

    private String token;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
