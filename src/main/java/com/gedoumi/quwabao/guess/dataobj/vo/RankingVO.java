package com.gedoumi.quwabao.guess.dataobj.vo;

import lombok.Data;

/**
 * 排名
 */
@Data
public class RankingVO {

    /**
     * 期号
     */
    private String issue;

    /**
     * 1号车名次
     */
    private String car1;

    /**
     * 2号车道名次
     */
    private String car2;

    /**
     * 3号车道名次
     */
    private String car3;

    /**
     * 4号车道名次
     */
    private String car4;

    /**
     * 5号车道名次
     */
    private String car5;

    /**
     * 6号车道名次
     */
    private String car6;

}
