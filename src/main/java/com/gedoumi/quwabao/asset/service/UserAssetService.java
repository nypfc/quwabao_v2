package com.gedoumi.quwabao.asset.service;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.asset.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.asset.mapper.UserAssetMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        return userAssetMapper.queryByUserId(userId);
    }

    /**
     * 创建用户资产
     *
     * @param userId 用户ID
     */
    public void createUserAsset(Long userId) {
        Date now = new Date();
        UserAsset userAsset = new UserAsset();
        userAsset.setUpdateTime(now);
        userAsset.setCreateTime(now);
        userAsset.setInitFrozenAsset(BigDecimal.ZERO);
        userAsset.setProfit(BigDecimal.ZERO);
        userAsset.setRemainAsset(BigDecimal.ZERO);
        userAsset.setInitBaseAsset(BigDecimal.ZERO);
        userAsset.setFrozenAsset(BigDecimal.ZERO);
        userAsset.setTotalAsset(BigDecimal.ZERO);
        userAsset.setUserId(userId);
        userAssetMapper.createUserAsset(userAsset);
    }

}
