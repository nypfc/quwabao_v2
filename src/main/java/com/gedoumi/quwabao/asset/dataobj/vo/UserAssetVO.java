package com.gedoumi.quwabao.asset.dataobj.vo;

import lombok.Data;

/**
 * 用户资产VO
 *
 * @author Minced
 */
@Data
public class UserAssetVO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 总收益
     */
    private String totalProfit;

    /**
     * 余额
     */
    private String remainAsset;

    /**
     * 天使钻
     */
    private String frozenAsset;

}
