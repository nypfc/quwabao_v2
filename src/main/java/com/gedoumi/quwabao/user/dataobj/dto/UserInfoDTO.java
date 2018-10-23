package com.gedoumi.quwabao.user.dataobj.dto;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.user.dataobj.model.User;
import lombok.Data;

/**
 * 用户信息传递对象
 *
 * @author Minced
 */
@Data
public class UserInfoDTO {

    /**
     * 用户信息
     */
    private User user;

    /**
     * 用户资产信息
     */
    private UserAsset userAsset;

}
