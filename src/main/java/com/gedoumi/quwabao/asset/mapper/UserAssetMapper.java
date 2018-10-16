package com.gedoumi.quwabao.asset.mapper;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资产Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserAssetMapper {

    /**
     * 创建用户资产
     *
     * @param userAsset 用户资产对象
     */
    void createUserAsset(UserAsset userAsset);

}
