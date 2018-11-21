package com.gedoumi.quwabao.trans.request.gateway;

import com.gedoumi.quwabao.common.constants.Constants;

public class ApiResponse {

    private int code;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSuccess(){
        this.setCode(Constants.API_SUCCESS_CODE);
        this.setMsg("ok");
    }

    public void setAccessError(){
        this.setCode(9001);
        this.setMsg("invalid access");
    }

    public void setAccountError(){
        this.setCode(2001);
        this.setMsg("unknown pfc_account");
    }
}
