package com.gedoumi.quwabao.trans.request.face.vo;

import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import lombok.Data;

@Data
public class FaceVO {

    private String mall_id;

    private String realname;

    private String idcard;

    private String tm;

    private String image;

    private String sign;

    public String generateSign() {
        StringBuilder params = new StringBuilder();
        params.append(getMall_id())
                .append(this.realname)
                .append(this.idcard)
                .append(this.tm)
                .append("d044af98dadcf991d39abe1c8d0b5e17");
        String sign = null;
        try {
            sign = MD5EncryptUtil.md5Encrypy(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

}
