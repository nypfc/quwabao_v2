package com.gedoumi.quwabao.api.gateway.vo;

import com.gedoumi.quwabao.common.utils.AesCBC;
import lombok.Data;

@Data
public class WithDrawVO {

    private String pfc_account;

    private String asset_name;

    private String amount;

    private String memo;

    private long ts;

    private long seq;

    private String sig;

    public String generateSign(String basePath) {
        StringBuilder params = new StringBuilder(basePath);
        params.append("amount")
                .append(this.amount)
                .append("asset_name")
                .append(this.asset_name)
                .append("memo")
                .append(this.memo)
                .append("pfc_account")
                .append(this.pfc_account)
                .append("seq")
                .append(this.seq)
                .append("ts")
                .append(this.ts);
        String sign = null;
        try {
            sign = AesCBC.encrypt(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

}
