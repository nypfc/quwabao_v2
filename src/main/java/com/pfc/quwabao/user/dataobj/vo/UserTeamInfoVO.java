package com.pfc.quwabao.user.dataobj.vo;

import lombok.Data;

/**
 * 用户团队信息
 *
 * @author Minced
 */
@Data
public class UserTeamInfoVO {

    /**
     * 用户团队等级
     */
    private Integer teamLevel;

    /**
     * 租用矿机价格总和
     */
    private String totalRentMoney;

}
