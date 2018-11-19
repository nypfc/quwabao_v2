package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.dto.UserAssetDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserAsset;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    UserAsset selectByUserId(Long userId);

    /**
     * 创建用户资产
     *
     * @param userAsset 用户资产对象
     * @return 数据库受影响行数
     */
    Integer insert(UserAsset userAsset);

    /**
     * 根据用户ID更新用户资产
     *
     * @param userId   用户ID
     * @param money    资产变动量
     * @param date     日期
     * @param isProfit 是否是收益
     * @return 数据库受影响行数
     */
    Integer updateByUserId(Long userId, BigDecimal money, Date date, Boolean isProfit);

    /**
     * 根据用户ID批量更新用户资产
     *
     * @param dtos 用户资产DTO集合
     * @return 数据库受影响行数
     */
    Integer updateBatchByUserId(List<UserAssetDTO> dtos);

}
