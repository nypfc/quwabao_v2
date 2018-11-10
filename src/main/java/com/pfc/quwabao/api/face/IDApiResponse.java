package com.pfc.quwabao.api.face;


import org.apache.commons.lang3.StringUtils;

public class IDApiResponse {

    private String status;
    private IDApiData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IDApiData getData() {
        return data;
    }

    public void setData(IDApiData data) {
        this.data = data;
    }

    public boolean isSucess(){
        if(getData() == null){
            return false;
        }
        if(StringUtils.equals("1000",getData().getCode())){
            return true;
        }
        return false;
    }
}
