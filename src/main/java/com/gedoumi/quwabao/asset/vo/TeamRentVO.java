package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TeamRentVO implements Serializable {


    private static final long serialVersionUID = -7948687903808962931L;

    private Long userId;
    private String mobile;

    private BigDecimal totalAsset;
    private Integer totalCount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(BigDecimal totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
