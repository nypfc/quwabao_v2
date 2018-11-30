package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;

/**
 * 用户信息VO
 *
 * @author Minced
 */
@Data
public class UserVO {

    /**
     * 用户信息VO
     */
    private UserInfoVO user = new UserInfoVO();

    @Data
    public class UserInfoVO {

        /**
         * 用户名
         */
        private String username;

        /**
         * 手机号
         */
        private String mobilePhone;

        /**
         * 邀请码
         */
        private String inviteCode;

    }

    /**
     * 用户资产VO
     */
    private UserAssetVO userAsset = new UserAssetVO();

    @Data
    public class UserAssetVO {

        /**
         * 总收益
         */
        private String totalProfit;

        /**
         * 余额
         */
        private String remainAsset;

    }

    /**
     * 用户团队VO
     */
    private UserTeamInfoVO userTeam = new UserTeamInfoVO();

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

}
