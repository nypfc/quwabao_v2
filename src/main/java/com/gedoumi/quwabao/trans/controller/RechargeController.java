package com.gedoumi.quwabao.trans.controller;

import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.trans.dataobj.form.RechargeForm;
import com.gedoumi.quwabao.trans.dataobj.vo.RechargeResponse;
import com.gedoumi.quwabao.trans.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.gedoumi.quwabao.common.constants.ApiConstants.APP_RECHARGE_CALLBACK;

/**
 * 充值回调Controller
 *
 * @author Minced
 */
@Slf4j
@RestController
public class RechargeController {

    @Resource
    private GatewayService gatewayService;

    /**
     * 充值回调
     *
     * @param rechargeForm 充值表单
     * @return 回调响应对象
     */
    @PostMapping(APP_RECHARGE_CALLBACK)
    public RechargeResponse recharge(RechargeForm rechargeForm) {
        log.info("recharge begin {}", JsonUtil.objectToJson(rechargeForm));
        RechargeResponse rechargeResponse = gatewayService.recharge(rechargeForm);
        log.info("recharge end ");
        return rechargeResponse;
    }

}
