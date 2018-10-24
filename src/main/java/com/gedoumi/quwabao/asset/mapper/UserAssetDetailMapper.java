package com.gedoumi.quwabao.asset.mapper;

import com.gedoumi.quwabao.asset.dataobj.model.UserAssetDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户资产详情Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserAssetDetailMapper {

    /**
     * 查询用户资产详情列表
     *
     * @param userId    用户ID
     * @param transType 需要查询的类型集合
     * @return 用户资产详情集合
     */
    List<UserAssetDetail> queryDetailList(Long userId, List<Integer> transType);

}
