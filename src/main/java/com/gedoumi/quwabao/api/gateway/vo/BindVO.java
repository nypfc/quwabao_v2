package com.gedoumi.quwabao.api.gateway.vo;

import com.gedoumi.quwabao.util.AesCBC;

import java.io.Serializable;

public class BindVO implements Serializable {


    private static final long serialVersionUID = 2141590934119685529L;
    private String pfc_account;

    private Long ts;

    private String sig;

    public String getPfc_account() {
        return pfc_account;
    }

    public void setPfc_account(String pfc_account) {
        this.pfc_account = pfc_account;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String generateSign(String basePath){
        StringBuilder params = new StringBuilder(basePath);
        params.append("pfc_account")
                .append(getPfc_account())
                .append("ts")
                .append(getTs());
        String sign = null;
        try {
            sign = AesCBC.encrypt(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}
