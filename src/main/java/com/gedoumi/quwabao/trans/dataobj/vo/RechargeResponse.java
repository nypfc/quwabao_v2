package com.gedoumi.quwabao.trans.dataobj.vo;

import com.gedoumi.quwabao.common.enums.RechargeStatusEnum;
import com.gedoumi.quwabao.trans.dataobj.dto.RechargeResponseData;
import lombok.Data;

/**
 * 充值回调响应
 *
 * @author Mincd
 */
@Data
public class RechargeResponse {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private RechargeResponseData data;

    /**
     * 成功的响应内容
     */
    public void success() {
        this.code = RechargeStatusEnum.SUCCESS.getValue();
        this.msg = RechargeStatusEnum.SUCCESS.getMessage();
    }

}
