package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;

/**
 * 用户团队
 *
 * @author Minced
 */
@Data
public class UserTeamVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 租用矿机数量
     */
    private Integer rentNumber;

}
