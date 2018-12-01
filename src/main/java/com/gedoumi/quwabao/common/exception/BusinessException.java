package com.gedoumi.quwabao.common.exception;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 *
 * @author Minced
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private CodeEnum codeEnum;

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

}
