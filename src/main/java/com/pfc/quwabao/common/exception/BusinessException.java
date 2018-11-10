package com.pfc.quwabao.common.exception;

import com.pfc.quwabao.common.enums.CodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private CodeEnum codeEnum;

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

}
