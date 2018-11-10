package com.pfc.quwabao.user.controller;

import com.pfc.quwabao.user.dataobj.model.UserAssetDetail;
import com.pfc.quwabao.user.dataobj.vo.UserAssetDetailVO;
import com.pfc.quwabao.user.service.UserAssetDetailService;
import com.pfc.quwabao.common.utils.ContextUtil;
import com.pfc.quwabao.common.utils.ResponseObject;
import com.pfc.quwabao.user.dataobj.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户资产详情Controller
 *
 * @author Minecd
 */
@RequestMapping("/v2/asset")
@RestController
public class UserAssetDetailController {

    @Resource
    private UserAssetDetailService userAssetDetailService;

    /**
     * 用户收益资产详情列表
     *
     * @param type 查询类型
     *             profit: 挖矿、推荐人收益等
     *             transacation: 转账、提现等
     * @return ResponseObject
     */
    @GetMapping("/{type}")
    public ResponseObject userProfitList(@NotBlank @PathVariable String type) {
        User user = ContextUtil.getUserFromRequest();
        List<UserAssetDetail> userAssetDetailList = userAssetDetailService.getUserAssetDetailList(user.getId(), type);
        // 封装返回信息
        List<UserAssetDetailVO> assetDetailVOList = userAssetDetailList.stream().map(userAssetDetail -> {
            UserAssetDetailVO assetDetailVO = new UserAssetDetailVO();
            assetDetailVO.setDay(userAssetDetail.getCreateTime());
            assetDetailVO.setTransType(userAssetDetail.getTransType());
            assetDetailVO.setTransMoney(String.valueOf(userAssetDetail.getMoney()));
            return assetDetailVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(assetDetailVOList);
    }

}
