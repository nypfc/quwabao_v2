package com.gedoumi.quwabao.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.mapper.UserAssetDetailMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.gedoumi.quwabao.common.constants.Constants.USER_DETAIL_VERSION;

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
     * 获取用户交易列表
     *
     * @param userId 用户ID
     * @param page   当前页码
     * @param rows   每页数据量
     * @return 资产详情集合
     */
    public List<UserAssetDetail> getUserTransactionDetails(Long userId, String page, String rows) {
        List<Integer> transTypeList = Lists.newArrayList(
                TransType.FrozenIn.getValue(),
                TransType.FrozenOut.getValue(),
                TransType.TeamInit.getValue(),
                TransType.TransIn.getValue(),
                TransType.TransOut.getValue(),
                TransType.Rent.getValue(),
                TransType.NetIn.getValue(),
                TransType.NetOut.getValue());
        // 分页查询
        Page<UserAssetDetail> p;
        try {
            p = new Page<>(Integer.parseInt(page), Integer.parseInt(rows));
        } catch (NumberFormatException ex) {
            log.error("分页参数不能格式化为int类型");
            throw new BusinessException(CodeEnum.ParamError);
        }
        userAssetDetailMapper.selectTransInIds(p, userId, transTypeList);
        return p.getRecords();
    }

    /**
     * 创建非挖矿收益用户资产详情
     *
     * @param userId 用户ID
     * @param money  资产变动量
     * @param type   资产类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserDetailAsset(Long userId, BigDecimal money, Integer type) {
        if (TransType.Profit.getValue() == type) {
            log.error("创建非挖矿收益资产详情时type不能为1");
            throw new BusinessException(CodeEnum.SysError);
        }
        createUserDetailAsset(userId, money, BigDecimal.ZERO, BigDecimal.ZERO, type);
    }

    /**
     * 创建挖矿收益的用户资产详情
     *
     * @param userId   用户ID
     * @param money    资产变动量
     * @param profit   不带本金收益（挖矿专用）
     * @param profitEx 带本金的收益（挖矿专用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserDetailAsset(Long userId, BigDecimal money, BigDecimal profit, BigDecimal profitEx) {
        createUserDetailAsset(userId, money, profit, profitEx, TransType.Profit.getValue());
    }

    /**
     * 创建用户资产详情
     *
     * @param userId   用户ID
     * @param money    资产变动量
     * @param profit   不带本金收益（挖矿专用）
     * @param profitEx 带本金的收益（挖矿专用）
     * @param type     资产类型
     */
    private void createUserDetailAsset(Long userId, BigDecimal money, BigDecimal profit, BigDecimal profitEx, Integer type) {
        UserAssetDetail detail = new UserAssetDetail();
        Date now = new Date();
        detail.setUserId(userId);
        detail.setMoney(money);
        // 如果变动类型为挖矿的收益，设置收益
        if (TransType.Profit.getValue() == type) {
            detail.setProfit(profit);
            detail.setProfitExt(profitEx);
        } else {
            detail.setProfit(BigDecimal.ZERO);
            detail.setProfitExt(BigDecimal.ZERO);
        }
        detail.setTransType(type);
        detail.setDigDate(now);
        detail.setCreateTime(now);
        detail.setUpdateTime(now);
        detail.setVersionType(USER_DETAIL_VERSION);
        userAssetDetailMapper.insert(detail);
    }

}
