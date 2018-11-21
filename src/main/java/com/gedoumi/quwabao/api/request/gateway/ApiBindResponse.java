package com.gedoumi.quwabao.api.request.gateway;

import com.gedoumi.quwabao.api.request.gateway.vo.QueryVO;

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
