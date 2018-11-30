package com.gedoumi.quwabao.trans.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SysLogStatusEnum;
import com.gedoumi.quwabao.common.enums.SysLogTypeEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.trans.dataobj.form.RechargeForm;
import com.gedoumi.quwabao.trans.dataobj.form.WithdrawForm;
import com.gedoumi.quwabao.trans.dataobj.vo.RechargeResponse;
import com.gedoumi.quwabao.trans.request.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserAssetService;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 请求API Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class GatewayService {

    @Resource
    private SysLogService sysLogService;

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private RedisCache redisCache;

    /**
     * 提现
     *
     * @param withdrawForm 提现表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(WithdrawForm withdrawForm) {
        // TODO 提现
    }

    /**
     * 获取以太坊地址
     * 如果用户已有以太坊地址，直接返回
     * 如果没有则先绑定以太坊地址
     *
     * @return 以太坊地址
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public String getEthAddress() {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        String mobile = user.getMobilePhone();
        // 如果用户已有以太坊地址，直接返回以太坊地址，否则先绑定地址
        String ethAddress = user.getEthAddress();
        if (StringUtils.isNotEmpty(ethAddress))
            return ethAddress;
        // 发送请求
        BindEthAddressRequest bind = new BindEthAddressRequest(mobile);
        BindEthAddressResponse response;
        try {
            response = bind.execute();
        } catch (Exception ex) {
            // 捕获请求异常并设置网关日志
            sysLogService.createSysLog(mobile, bind, SysLogTypeEnum.BIND_ETH_ADDRESS.getValue(), SysLogStatusEnum.FAIL.getValue());
            // 抛出业务异常
            throw new BusinessException(CodeEnum.GateWayError);
        }
        // 判断结果
        Integer code = response.getCode();
        if (code == 0) {
            String bindEthAddress = response.getData().getEth_address();
            if (StringUtils.isNotEmpty(bindEthAddress)) {
                // 请求成功的网关日志
                sysLogService.createSysLog(mobile, bind, SysLogTypeEnum.BIND_ETH_ADDRESS.getValue(), SysLogStatusEnum.SUCCESS.getValue());
                // 更新用户以太坊地址与更新缓存
                user.setEthAddress(bindEthAddress);
                userService.updateById(user);
                redisCache.setKeyValueData(user.getToken(), user);
                return bindEthAddress;
            }
        }
        // 错误的请求日志
        log.error("网关响应失败:{}，信息:{}", code, response.getMsg());
        sysLogService.createSysLog(mobile, bind, SysLogTypeEnum.BIND_ETH_ADDRESS.getValue(), SysLogStatusEnum.FAIL.getValue());
        throw new BusinessException(CodeEnum.GateWayError);
    }

    /**
     * 充值回调
     *
     * @param rechargeForm 充值表单
     * @return 回调响应对象
     */
    public RechargeResponse recharge(RechargeForm rechargeForm) {
        // 创建响应对象
        RechargeResponse rechargeResponse = new RechargeResponse();
        // 获取参数
        String mobile = rechargeForm.getPfc_account();
        String assetName = rechargeForm.getAsset_name();
        String amount = rechargeForm.getAmount();
        String seq = rechargeForm.getSeq();
        String ts = rechargeForm.getTs();
        String sign = rechargeForm.getSig();
        // 参数非空验证
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(assetName) || StringUtils.isEmpty(amount)
                || StringUtils.isEmpty(seq) || StringUtils.isEmpty(ts) || StringUtils.isEmpty(sign)) {
            log.error("参数非空校验失败");
            rechargeResponse.acessError();
            return rechargeResponse;
        }
        // 验证签名
        if (!StringUtils.equals(sign, rechargeForm.parameterSign())) {
            log.error("签名错误");
            rechargeResponse.acessError();
            return rechargeResponse;
        }
        // 验证合法性验证
        try {
            Long.parseLong(ts);
        } catch (NumberFormatException ex) {
            log.error("时间戳：{}格式不正确", ts);
            rechargeResponse.acessError();
            return rechargeResponse;
        }
        BigDecimal money;
        try {
            money = new BigDecimal(amount);
        } catch (NumberFormatException ex) {
            log.error("金额：{}格式不正确", amount);
            rechargeResponse.acessError();
            return rechargeResponse;
        }
        User user = userService.getByMobile(mobile);
        if (user == null) {
            log.error("pfc_account：{}不存在", mobile);
            rechargeResponse.accountError();
            return rechargeResponse;
        }
        // 如果资产详情表中已经有对应的充值ID，说明已经充值完成，不增加资产
        Long userId = user.getId();
        if (userAssetDetailService.getBySeq(seq) == 0) {
            // 更新资产
            userAssetService.updateUserAsset(userId, money, false);
            // 创建资产详情
            userAssetDetailService.insertUserDetailAsset(userId, null, money, null, BigDecimal.ZERO,
                    BigDecimal.ZERO, TransTypeEnum.NetIn.getValue(), null, seq);
        }

        // 封装返回数据
        RechargeResponse.ResponseData data = rechargeResponse.new ResponseData();
        data.setPfc_account(mobile);
        data.setEth_address(user.getEthAddress());
        rechargeResponse.success();
        rechargeResponse.setData(data);
        return rechargeResponse;
    }

}
