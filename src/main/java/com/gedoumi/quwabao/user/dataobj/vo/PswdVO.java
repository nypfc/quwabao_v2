package com.gedoumi.quwabao.user.dataobj.vo;

import java.io.Serializable;

public class PswdVO implements Serializable {


    private static final long serialVersionUID = -3188186112962416718L;

    private String orgPswd;

    private String pswd;

    public String getOrgPswd() {
        return orgPswd;
    }

    public void setOrgPswd(String orgPswd) {
        this.orgPswd = orgPswd;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
