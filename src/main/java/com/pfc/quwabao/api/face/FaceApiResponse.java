package com.pfc.quwabao.api.face;


import org.apache.commons.lang3.StringUtils;

public class FaceApiResponse {

    private String result;
    private FaceApiData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public FaceApiData getData() {
        return data;
    }

    public void setData(FaceApiData data) {
        this.data = data;
    }

    public boolean isSucess(){
        if(getData() == null){
            return false;
        }
        if(StringUtils.equals("1",getResult())){
            return true;
        }
        return false;
    }
}
