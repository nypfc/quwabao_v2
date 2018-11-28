package com.gedoumi.quwabao.trans.controller;

import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.trans.dataobj.form.RechargeForm;
import com.gedoumi.quwabao.trans.dataobj.vo.RechargeResponse;
import com.gedoumi.quwabao.trans.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @PostMapping(value = "/api/v2/pfc/recharge")
    public RechargeResponse recharge(RechargeForm rechargeForm) {
        log.info("recharge begin {}", JsonUtil.objectToJson(rechargeForm));
        return gatewayService.recharge(rechargeForm);
    }

}
