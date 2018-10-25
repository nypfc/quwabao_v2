package com.gedoumi.quwabao.asset.service;

import com.gedoumi.quwabao.asset.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.asset.mapper.UserAssetDetailMapper;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户资产详情Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserAssetDetailService {

    @Resource
    private UserAssetDetailMapper userAssetDetailMapper;

    /**
     * 获取用户资产详情列表
     *
     * @param userId 用户ID
     * @param type   查询类型
     *               profit: 挖矿、推荐人收益等
     *               transacation: 转账、提现等
     * @return 资产详情集合
     */
    public List<UserAssetDetail> getUserAssetDetailList(Long userId, String type) {
        List<Integer> transTypeList;
        switch (type) {
            case "profit":
                // TODO 后面添加俱乐部收益（绩差收益）
                transTypeList = Lists.newArrayList(TransType.Profit.getValue(), TransType.Reward.getValue());
                break;
            case "trans":
                transTypeList = Lists.newArrayList(TransType.TransOut.getValue(), TransType.TransIn.getValue(),
                        TransType.NetIn.getValue(), TransType.NetOut.getValue(), TransType.FrozenIn.getValue());
                break;
            default:
                log.error("查询资产详情列表的type参数有误，type:{}", type);
                throw new BusinessException(CodeEnum.ParamError);
        }
        return userAssetDetailMapper.queryDetailList(userId, transTypeList);
    }

}
