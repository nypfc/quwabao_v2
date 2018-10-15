package com.gedoumi.quwabao.user.dataobj.vo;

import java.io.Serializable;

public class ValidateUserVO implements Serializable {

    private static final long serialVersionUID = 1532891081016449848L;

    private String realName;

    private String idCard;

    private String base64Img;

    private Long userId;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBase64Img() {
        if(base64Img == null){
            return "";
        }
        if(base64Img.contains("base64,")){
            return base64Img.split("base64,")[1];
        }
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
