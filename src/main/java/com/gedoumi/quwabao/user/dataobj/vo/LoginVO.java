package com.gedoumi.quwabao.user.dataobj.vo;

import java.io.Serializable;

public class LoginVO implements Serializable {


    private static final long serialVersionUID = 3228417536946042542L;
    private String username;

//    private String validateCode;
//
//    private String smsCode;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
