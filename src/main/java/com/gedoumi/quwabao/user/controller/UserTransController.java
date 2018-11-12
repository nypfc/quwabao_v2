package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.enums.UserProfitVOEnum;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import com.gedoumi.quwabao.user.dataobj.vo.UserAssetDetailVO;
import com.gedoumi.quwabao.user.dataobj.vo.UserProfitVO;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserProfitService;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户交易/收益Controller
 *
 * @author Minecd
 */
@RequestMapping("/v2/user")
@RestController
public class UserTransController {

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private UserProfitService userProfitService;

    /**
     * 用户交易详情列表
     *
     * @param page 当前页码
     * @param rows 每页数据量
     * @return ResponseObject
     */
    @GetMapping("/trans/{page}/{rows}")
    public ResponseObject userTransactionList(@PathVariable String page, @PathVariable String rows) {
        User user = ContextUtil.getUserFromRequest();
        List<UserAssetDetail> userAssetDetailList = userAssetDetailService.getUserTransactionDetails(user.getId(), page, rows);
        // 封装返回信息
        List<UserAssetDetailVO> assetDetailVOs = userAssetDetailList.stream().map(userAssetDetail -> {
            UserAssetDetailVO assetDetailVO = new UserAssetDetailVO();
            assetDetailVO.setDay(userAssetDetail.getCreateTime());
            assetDetailVO.setTransType(userAssetDetail.getTransType());
            assetDetailVO.setTransMoney(String.valueOf(userAssetDetail.getMoney()));
            return assetDetailVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(assetDetailVOs);
    }

    /**
     * 用户收益详情列表
     *
     * @param page 当前页码
     * @param rows 每页数据量
     * @return ResponseObject
     */
    @GetMapping("/profit/{page}/{rows}")
    public ResponseObject userProfits(@PathVariable String page, @PathVariable String rows) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 遍历结果，封装数据
        List<UserProfitVO> profitVOs = Lists.newArrayList();
        List<UserProfit> profits = userProfitService.getUserProfits(user.getId(), page, rows);
        profits.forEach(userProfit -> {
            //日期
            Date date = userProfit.getDate();
            // 静态收益数据
            BigDecimal staticProfit = userProfit.getStaticProfit();
            if (staticProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO static_ = new UserProfitVO();
                static_.setDate(date);
                static_.setType(UserProfitVOEnum.STATIC_PROFIT.getValue());
                static_.setProfit(staticProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(static_);
            }
            // 动态收益数据
            BigDecimal dynamicProfit = userProfit.getDynamicProfit();
            if (dynamicProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO dynamic_ = new UserProfitVO();
                dynamic_.setDate(date);
                dynamic_.setType(UserProfitVOEnum.DYNAMIC_PROFIT.getValue());
                dynamic_.setProfit(dynamicProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(dynamic_);
            }
            // 俱乐部收益数据
            BigDecimal clubProfit = userProfit.getClubProfit();
            if (clubProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO club_ = new UserProfitVO();
                club_.setDate(date);
                club_.setType(UserProfitVOEnum.CLUB_PROFIT.getValue());
                club_.setProfit(clubProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(club_);
            }
        });
        return new ResponseObject<>(profitVOs);
    }

}
