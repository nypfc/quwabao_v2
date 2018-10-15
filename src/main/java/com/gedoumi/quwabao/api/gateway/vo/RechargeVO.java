package com.gedoumi.quwabao.api.gateway.vo;

import com.gedoumi.quwabao.util.AesCBC;

import java.io.Serializable;

public class RechargeVO implements Serializable {


    private static final long serialVersionUID = -5367570226686145655L;

    private String pfc_account;

    private String asset_name;

    private String amount;

    private Long ts;

    private String seq;

    private String sig;

    public String getPfc_account() {
        return pfc_account;
    }

    public void setPfc_account(String pfc_account) {
        this.pfc_account = pfc_account;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String generateSign(String basePath){
        StringBuilder params = new StringBuilder(basePath);
        params.append("amount")
                .append(getAmount())
                .append("asset_name")
                .append(getAsset_name())
                .append("pfc_account")
                .append(getPfc_account())
                .append("seq")
                .append(getSeq())
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
