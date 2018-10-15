package com.gedoumi.quwabao.common.base;

import com.gedoumi.quwabao.common.enums.CodeEnum;

import java.io.Serializable;

public class ResponseObject implements Serializable {


    private static final long serialVersionUID = -3012757815222534900L;

    private Object data;

    private String code;

    private String message;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(){
        this.setCode(CodeEnum.Success.getCode());
        this.setMessage(CodeEnum.Success.getMessage());
    }

    public void setInfo(CodeEnum codeEnum){
        if(codeEnum != null){
            this.setCode(codeEnum.getCode());
            this.setMessage(codeEnum.getMessage());
        }

    }
}
