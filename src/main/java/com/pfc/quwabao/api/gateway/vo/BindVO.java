package com.pfc.quwabao.api.gateway.vo;

import com.pfc.quwabao.common.utils.AesCBC;
import lombok.Data;

@Data
public class BindVO {

    private String pfc_account;

    private Long ts;

    private String sig;

    public String generateSign(String basePath) {
        StringBuilder params = new StringBuilder(basePath);
        params.append("pfc_account")
                .append(this.pfc_account)
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
