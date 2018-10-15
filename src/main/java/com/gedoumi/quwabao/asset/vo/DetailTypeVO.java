package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;

public class DetailTypeVO implements Serializable {
    private Long userId;
    private int detailType;
    private int page = 1;
    private int rows = 20;

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
