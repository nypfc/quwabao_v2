package com.gedoumi.quwabao.user.dataobj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户收益VO
 *
 * @author Minced
 */
@Data
public class UserProfitVO {

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    /**
     * 类型
     * 1:静态收益
     * 2:动态收益
     * 3:俱乐部收益
     */
    private Integer type;

    /**
     * 收益量
     */
    private String profit;

}
