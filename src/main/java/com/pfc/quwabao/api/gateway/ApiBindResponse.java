package com.pfc.quwabao.api.gateway;

import com.pfc.quwabao.api.gateway.vo.QueryVO;

public class ApiBindResponse extends ApiResponse{

    private QueryVO data;

    @Override
    public QueryVO getData() {
        return data;
    }

    public void setData(QueryVO data) {
        this.data = data;
    }
}
