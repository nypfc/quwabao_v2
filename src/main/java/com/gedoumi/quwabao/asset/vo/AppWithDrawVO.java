package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;

public class AppWithDrawVO implements Serializable {

    private static final long serialVersionUID = 5396763172600701930L;

    private String pswd;

    private String amount;

    private String ethAddress;

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }
}
