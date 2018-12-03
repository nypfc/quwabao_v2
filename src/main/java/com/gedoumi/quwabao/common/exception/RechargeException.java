package com.gedoumi.quwabao.common.exception;

import com.gedoumi.quwabao.common.enums.RechargeStatusEnum;
import lombok.Getter;

/**
 * 充值异常
 *
 * @author Minecd
 */
@Getter
public class RechargeException extends RuntimeException {

    private RechargeStatusEnum rechargeStatusEnum;

    public RechargeException(RechargeStatusEnum rechargeStatusEnum) {
        super(rechargeStatusEnum.getMessage());
        this.rechargeStatusEnum = rechargeStatusEnum;
    }

}
