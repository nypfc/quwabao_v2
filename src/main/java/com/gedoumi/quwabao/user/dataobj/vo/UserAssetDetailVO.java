package com.gedoumi.quwabao.user.dataobj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserAssetDetailVO {

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date day;

    /**
     * 交易类型
     */
    private Integer transType;

    /**
     * 交易金额
     */
    private String transMoney;

}
