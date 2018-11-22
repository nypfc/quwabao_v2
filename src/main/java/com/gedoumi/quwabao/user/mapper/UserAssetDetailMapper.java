package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Date;
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
    List<UserAssetDetail> selectTransInIds(Long userId, List<Integer> transType);

    /**
     * 查询当日已经提现的总金额
     *
     * @param userId        用户ID
     * @param transType     交易类型
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 已经提现的总金额
     */
    BigDecimal selectTotalDayWithdraw(Long userId, Integer transType, Date startDateTime, Date endDateTime);

    /**
     * 创建用户资产详情
     *
     * @param userAssetDetail 用户资产详情
     * @return 数据库受影响行数
     */
    Integer insert(UserAssetDetail userAssetDetail);

    /**
     * 批量创建用户资产详情
     *
     * @param details 用户资产详情集合
     * @return 数据库受影响行数
     */
    Integer insertBatch(List<UserAssetDetail> details);

    /**
     * 根据ID更新用户资产详情
     *
     * @param userAssetDetail 用户资产详情
     * @return 数据库受影响行数
     */
    Integer updateById(UserAssetDetail userAssetDetail);

}
