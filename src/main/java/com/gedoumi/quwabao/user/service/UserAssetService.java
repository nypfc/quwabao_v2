package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.dataobj.model.UserAsset;
import com.gedoumi.quwabao.user.mapper.UserAssetMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * 用户资产Service
 *
 * @author Minced
 */
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
        return Optional.ofNullable(userAssetMapper.queryByUserId(userId)).orElseGet(() -> createUserAsset(userId));
    }

    /**
     * 创建用户资产
     *
     * @param userId 用户ID
     * @return 用户资产对象
     */
    @Transactional(rollbackFor = Exception.class)
    public UserAsset createUserAsset(Long userId) {
        UserAsset userAsset = new UserAsset();
        userAsset.setProfit(BigDecimal.ZERO);
        userAsset.setRemainAsset(BigDecimal.ZERO);
        userAsset.setFrozenAsset(BigDecimal.ZERO);
        userAsset.setTotalAsset(BigDecimal.ZERO);
        userAsset.setUserId(userId);
        userAssetMapper.createUserAsset(userAsset);
        return userAsset;
    }

}
