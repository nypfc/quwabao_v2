package com.gedoumi.quwabao.common.base;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import lombok.Data;

/**
 * 错误响应对象
 *
 * @author Minced
 */
@Data
public class ErrorResponseObject {

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    public ErrorResponseObject(String message) {
        this.code = "0001";
        this.message = message;
    }

    public ErrorResponseObject(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

}
