package com.gedoumi.quwabao.api.gateway;

import com.gedoumi.quwabao.api.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.common.annotation.PfcLogAspect;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/pfc")
public class PfcApiController {

    @PfcLogAspect
    @PostMapping(value = "/recharge")
    public ApiResponse recharge(RechargeVO rechargeVO) {
        log.info("recharge begin {}", JsonUtil.objectToJson(rechargeVO));

//        ApiResponse apiResponse = new ApiResponse();
//        if (rechargeVO == null || rechargeVO.getTs() == null
//                || StringUtils.isEmpty(rechargeVO.getSeq())
//                || StringUtils.isEmpty(rechargeVO.getPfc_account())
//                || StringUtils.isEmpty(rechargeVO.getAsset_name())
//                || StringUtils.isEmpty(rechargeVO.getAmount())
//                || StringUtils.isEmpty(rechargeVO.getSig())) {
//            apiResponse.setAccessError();
//            return apiResponse;
//        }
//
//        String sign = rechargeVO.generateSign(TransApi.path);
//
//        if (!StringUtils.equals(sign, rechargeVO.getSig())) {
//            apiResponse.setAccessError();
//            return apiResponse;
//        }
//        User user = userService.getByMobilePhone(rechargeVO.getPfc_account());
//        if (user == null) {
//            logger.info("{}用户不存在", rechargeVO.getPfc_account());
//            apiResponse.setAccountError();
//            return apiResponse;
//        }
//
//        UserAssetDetail assetDetail = assetDetailService.getByApiTransSeq(rechargeVO.getSeq());
//        if (assetDetail == null) {
//
//            UserAssetDetail detail = new UserAssetDetail();
//            detail.setUser(user);
//            detail.setMoney(new BigDecimal(rechargeVO.getAmount()));
//            detail.setProfit(BigDecimal.ZERO);
//            detail.setProfitExt(BigDecimal.ZERO);
//            detail.setTransType(TransType.NetIn.getValue());
//            detail.setApiTransSeq(rechargeVO.getSeq());
//            userAssetService.addAsset(detail);
//
//        }
//        QueryVO queryVO = new QueryVO();
//        queryVO.setPfc_account(user.getMobilePhone());
//        queryVO.setEth_address(user.getEthAddress());
//        apiResponse.setData(queryVO);
//        apiResponse.setSuccess();
//        logger.info("recharge end ");
        return null;
    }


}
