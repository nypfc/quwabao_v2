package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithDrawInfoVO implements Serializable {


    private static final long serialVersionUID = -3143799974281641642L;

    private BigDecimal singleLimit;

    private BigDecimal dayLimit;

    private BigDecimal remainLimit;

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getRemainLimit() {
        return remainLimit;
    }

    public void setRemainLimit(BigDecimal remainLimit) {
        this.remainLimit = remainLimit;
    }
}
