package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

@Data
public class RegForm {

    private String mobile;

    private String validateCode;

    private String smsCode;

    private String password;

    private String regInviteCode;

    private String userName;

}
