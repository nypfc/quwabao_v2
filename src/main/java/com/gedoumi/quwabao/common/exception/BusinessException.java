package com.gedoumi.quwabao.common.exception;

import com.gedoumi.quwabao.common.enums.CodeEnum;

public class BusinessException extends RuntimeException {

    private CodeEnum codeEnum;

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(CodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }
}
