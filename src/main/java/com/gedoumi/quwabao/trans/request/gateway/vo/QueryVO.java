package com.gedoumi.quwabao.trans.request.gateway.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryVO extends BindVO {

    private String eth_address;

}
