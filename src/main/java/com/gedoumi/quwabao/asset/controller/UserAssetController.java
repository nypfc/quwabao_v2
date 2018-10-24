package com.gedoumi.quwabao.asset.controller;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.asset.dataobj.vo.UserAssetVO;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户资产Controller
 *
 * @author Minced
 */
@RequestMapping("/v2/asset")
@RestController
public class UserAssetController {

    @Resource
    private UserAssetService userAssetService;

    /**
     * 获取用户资产信息
     *
     * @return ResponseObject
     */
    @GetMapping("/info")
    public ResponseObject userAssetInfo() {
        // 获取用户信息
        User user = ContextUtil.getUserFromRequest();

        UserAsset userAsset = userAssetService.getUserAsset(user.getId());
        // 封装返回信息
        UserAssetVO userAssetVO = new UserAssetVO();
        userAssetVO.setMobile(user.getMobilePhone());
        userAssetVO.setTotalProfit(String.valueOf(userAsset.getProfit()));
        userAssetVO.setFrozenAsset(String.valueOf(userAsset.getFrozenAsset()));
        userAssetVO.setRemainAsset(String.valueOf(userAsset.getRemainAsset()));
        return new ResponseObject<>(userAssetVO);
    }

}
