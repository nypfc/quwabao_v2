package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;


@Data
public class ValidateUserVO {

    private String realName;

    private String idCard;

    private String base64Img;

    private Long userId;

}
