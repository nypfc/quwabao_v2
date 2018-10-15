package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;

public class RentVO implements Serializable {


    private static final long serialVersionUID = 6152847335444493064L;

    private Integer rentType;

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }
}
