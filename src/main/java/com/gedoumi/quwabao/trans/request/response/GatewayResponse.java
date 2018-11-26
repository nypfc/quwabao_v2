package com.gedoumi.quwabao.trans.request.response;

import lombok.Data;

/**
 * 网关响应
 *
 * @author Minced
 */
@Data
public class GatewayResponse {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

}
