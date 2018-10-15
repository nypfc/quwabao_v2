package com.gedoumi.quwabao.asset.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class InitBaseAssetVO implements Serializable {


    private static final long serialVersionUID = -4628352206155189276L;

    private BigDecimal totalAsset;

}
