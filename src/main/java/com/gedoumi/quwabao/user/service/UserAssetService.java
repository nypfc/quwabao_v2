package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.dto.UserAssetDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserAsset;
import com.gedoumi.quwabao.user.mapper.UserAssetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 用户资产Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserAssetService {

    @Resource
    private UserAssetMapper userAssetMapper;

    /**
     * 获取用户资产
     *
     * @param userId 用户ID
     * @return 用户资产对象
     */
    public UserAsset getUserAsset(Long userId) {
        // 未查询到资产创建资产
        return Optional.ofNullable(userAssetMapper.selectByUserId(userId)).orElseGet(() -> createUserAsset(userId));
    }

    /**
     * 初始化用户资产
     *
     * @param userId 用户ID
     * @return 用户资产对象
     */
    @Transactional(rollbackFor = Exception.class)
    public UserAsset createUserAsset(Long userId) {
        UserAsset userAsset = new UserAsset();
        Date now = new Date();
        userAsset.setCreateTime(now);
        userAsset.setUpdateTime(now);
        userAsset.setProfit(BigDecimal.ZERO);
        userAsset.setRemainAsset(BigDecimal.ZERO);
        userAsset.setFrozenAsset(BigDecimal.ZERO);
        userAsset.setTotalAsset(BigDecimal.ZERO);
        userAsset.setUserId(userId);
        userAsset.setInitBaseAsset(BigDecimal.ZERO);  // 冗余字段
        userAsset.setInitFrozenAsset(BigDecimal.ZERO);  // 冗余字段
        userAssetMapper.insert(userAsset);
        return userAsset;
    }

    /**
     * 查询余额
     *
     * @param userId 用户ID
     * @param money  交易量
     */
    public void remainAsset(Long userId, BigDecimal money) {
        // 查询用户资产
        UserAsset userAsset = Optional.ofNullable(userAssetMapper.selectByUserId(userId)).orElseThrow(() -> {
            log.error("用户:{}资产不存在", userId);
            return new BusinessException(CodeEnum.RemainAssetError);
        });
        // 余额判断
        if (userAsset.getRemainAsset().compareTo(money) < 0) {
            log.error("用户:{}余额不足", userId);
            throw new BusinessException(CodeEnum.RemainAssetError);
        }
    }

    /**
     * 更新用户资产
     *
     * @param userId   用户ID
     * @param money    资产变更量
     * @param isProfit 是否是收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAsset(Long userId, BigDecimal money, Boolean isProfit) {
        userAssetMapper.updateByUserId(userId, money, new Date(), isProfit);
    }

    /**
     * 批量更新用户资产
     *
     * @param dtos 用户资产DTO集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<UserAssetDTO> dtos) {
        userAssetMapper.updateBatchByUserId(dtos);
    }

}
