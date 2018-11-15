package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.mapper.UserAssetDetailMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
     * 获取用户交易列表
     *
     * @param userId 用户ID
     * @param page   当前页码
     * @param rows   每页数据量
     * @return 资产详情集合
     */
    public List<UserAssetDetail> getUserTransactionDetails(Long userId, String page, String rows) {
        List<Integer> transTypeList = Lists.newArrayList(
                TransTypeEnum.FrozenIn.getValue(),
                TransTypeEnum.FrozenOut.getValue(),
                TransTypeEnum.TeamInit.getValue(),
                TransTypeEnum.TransIn.getValue(),
                TransTypeEnum.TransOut.getValue(),
                TransTypeEnum.Rent.getValue(),
                TransTypeEnum.NetIn.getValue(),
                TransTypeEnum.NetOut.getValue());
        // 分页查询
        try {
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        } catch (NumberFormatException ex) {
            log.error("分页参数不能格式化为int类型");
            throw new BusinessException(CodeEnum.ParamError);
        }
        PageInfo<UserAssetDetail> p = new PageInfo<>(userAssetDetailMapper.selectTransInIds(userId, transTypeList));
        return p.getList();
    }

    /**
     * 创建用户资产详情
     *
     * @param detail 用户资产详情
     */
    public void insertUserDetailAsset(UserAssetDetail detail) {
        userAssetDetailMapper.insert(detail);
    }

    /**
     * 批量创建用户资产详情
     *
     * @param details 用户资产详情集合
     */
    public void insertBatch(List<UserAssetDetail> details) {
        userAssetDetailMapper.insertBatch(details);
    }

}
