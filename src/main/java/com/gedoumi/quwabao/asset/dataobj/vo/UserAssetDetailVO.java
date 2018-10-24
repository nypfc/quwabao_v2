package com.gedoumi.quwabao.asset.dataobj.vo;

import lombok.Data;

@Data
public class UserAssetDetailVO {

    /**
     * 日期
     */
    private String day;

    /**
     * 交易类型
     */
    private Integer transType;

    /**
     * 交易金额
     */
    private String transMoney;

}
