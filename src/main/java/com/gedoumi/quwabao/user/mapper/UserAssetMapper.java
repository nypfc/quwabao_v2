package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserAsset;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资产Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserAssetMapper {

    /**
     * 根据用户ID查询用户资产
     *
     * @param userId 用户ID
     * @return 用户资产对象
     */
    UserAsset queryByUserId(Long userId);

    /**
     * 创建用户资产
     *
     * @param userAsset 用户资产对象
     */
    void createUserAsset(UserAsset userAsset);

}
