package com.gedoumi.quwabao.api.face.vo;

import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;

import java.io.Serializable;

public class FaceVO implements Serializable {


    private static final long serialVersionUID = 5469485249693435035L;

    private String mall_id;

    private String realname;

    private String idcard;

    private String tm;

    private String image ;

    private String sign;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMall_id() {
        return mall_id;
    }

    public void setMall_id(String mall_id) {
        this.mall_id = mall_id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String generateSign(){
        StringBuilder params = new StringBuilder();
        params.append(getMall_id())
                .append(getRealname())
                .append(getIdcard())
                .append(getTm())
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
