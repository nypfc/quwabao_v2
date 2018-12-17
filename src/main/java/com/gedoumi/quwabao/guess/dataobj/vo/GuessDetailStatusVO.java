package com.gedoumi.quwabao.guess.dataobj.vo;

import lombok.Data;

/**
 * 竞猜详情状态VO
 *
 * @author Minced
 */
@Data
public class GuessDetailStatusVO {

    /**
     * 下场时间
     */
    private String next;

    /**
     * 状态
     */
    private Integer status;

}
