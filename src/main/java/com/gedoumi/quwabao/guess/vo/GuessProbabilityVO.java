package com.gedoumi.quwabao.guess.vo;

import lombok.Data;

/**
 * 竞猜几率
 * @author Minced
 */
@Data
public class GuessProbabilityVO {

    /**
     * 玩法一赔率集合
     */
    private Odds1VO mode1;

    /**
     * 玩法二赔率集合
     */
    private Odds2VO mode2;

    /**
     * 玩法三赔率集合
     */
    private Odds3VO mode3;

    /**
     * 用户余额
     */
    private String userRemainAsset;

    /**
     * 剩余时间，单位：秒
     */
    private Integer remain;

}
