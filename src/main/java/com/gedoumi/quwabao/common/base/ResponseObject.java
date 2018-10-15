package com.gedoumi.quwabao.common.base;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import lombok.Data;

@Data
public class ResponseObject<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public ResponseObject() {
        this.setCode(CodeEnum.Success.getCode());
        this.setMessage(CodeEnum.Success.getMessage());
    }

    public ResponseObject(T data) {
        this.setCode(CodeEnum.Success.getCode());
        this.setMessage(CodeEnum.Success.getMessage());
        this.setData(data);
    }

    public ResponseObject(CodeEnum codeEnum) {
        this.setCode(codeEnum.getCode());
        this.setMessage(codeEnum.getMessage());
    }

}
