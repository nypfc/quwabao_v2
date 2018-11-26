package com.gedoumi.quwabao.trans.controller;

import com.gedoumi.quwabao.common.annotation.DuplicateRequest;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.service.SysConfigService;
import com.gedoumi.quwabao.trans.dataobj.form.WithdrawForm;
import com.gedoumi.quwabao.trans.dataobj.vo.WithdrawInfoVO;
import com.gedoumi.quwabao.trans.service.GatewayService;
import com.gedoumi.quwabao.trans.service.TransDetailService;
import com.gedoumi.quwabao.user.dataobj.form.TransferForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 交易Controller
 *
 * @author Minced
 */
@Slf4j
@RequestMapping("/v2/trans")
@RestController
public class TransacationController {

    @Resource
    private TransDetailService transDetailService;

    @Resource
    private GatewayService gatewayService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * APP内用户转账
     *
     * @param transferForm 转账表单
     * @return ResponseObject
     */
    @PostMapping("/transfer")
    public ResponseObject transfer(@RequestBody @Valid TransferForm transferForm) {
        transDetailService.transfer(transferForm);
        return new ResponseObject();
    }

    /**
     * 提现
     *
     * @param withdrawForm 提现表单
     * @return ResponseObject
     */
    @DuplicateRequest  // 防止重复提交
    @PostMapping("/withdraw")
    public ResponseObject withdraw(@RequestBody WithdrawForm withdrawForm) {
        gatewayService.withdraw(withdrawForm);
        return new ResponseObject();
    }

    /**
     * 获取以太坊地址
     *
     * @return ResponseObject
     */
    @GetMapping("/ethAddress")
    public ResponseObject getEthAddress() {
        return new ResponseObject<>(gatewayService.getEthAddress());
    }

    /**
     * 获取提现信息
     *
     * @return ResponseObject
     */
    @GetMapping("/withdraw/info")
    public ResponseObject withdrawInfo() {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取系统配置
        SysConfig sysConfig = sysConfigService.getSysConfig();
        // 封装返回信息
        WithdrawInfoVO withdrawInfoVO = new WithdrawInfoVO();
        withdrawInfoVO.setSingleMin(sysConfig.getWithdrawSingleMin());
        withdrawInfoVO.setSingleMax(sysConfig.getWithdrawSingleMax());
        withdrawInfoVO.setDayLimit(sysConfig.getWithdrawDayLimit());
        withdrawInfoVO.setRemainLimit(userAssetDetailService.getTotalDayWithdraw(user.getId()));
        return new ResponseObject<>(withdrawInfoVO);
    }

}
