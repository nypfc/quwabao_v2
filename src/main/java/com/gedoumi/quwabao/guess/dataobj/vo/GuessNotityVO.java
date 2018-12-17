package com.gedoumi.quwabao.guess.dataobj.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 开始比赛的通知VO
 * @author Minced
 */
@Data
public class GuessNotityVO {

    /**
     * 通知类型
     */
    private Integer type;

    /**
     * 结束通知信息
     */
    private String message;

    /**
     * 数据
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

}
