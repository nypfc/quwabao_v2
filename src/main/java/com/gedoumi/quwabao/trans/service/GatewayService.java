package com.gedoumi.quwabao.trans.service;

import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.exception.RechargeException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.IdGen;
import com.gedoumi.quwabao.common.utils.PasswordUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.service.SysConfigService;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.trans.dataobj.dto.RechargeResponseData;
import com.gedoumi.quwabao.trans.dataobj.form.RechargeForm;
import com.gedoumi.quwabao.trans.dataobj.form.WithdrawForm;
import com.gedoumi.quwabao.trans.request.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.WithdrawRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import com.gedoumi.quwabao.trans.request.response.WithdrawResponse;
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
import java.util.Optional;

/**
 * 请求API Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class GatewayService {

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private RedisCache redisCache;

    /**
     * 提现
     *
     * @param withdrawForm 提现表单
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public void withdraw(WithdrawForm withdrawForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        Long userId = user.getId();
        String mobile = user.getMobilePhone();
        String userPayPassword = user.getPayPassword();
        String ethAddress = user.getEthAddress();
        // 获取参数
        String amount = withdrawForm.getAmount();
        BigDecimal amountBigDecimal = new BigDecimal(withdrawForm.getAmount());
        String payPassword = withdrawForm.getPassword();
        // 支付密码验证
        PasswordUtil.payPasswordValidate(userId, userPayPassword, payPassword);
        // 条件验证
        SysConfig sysConfig = sysConfigService.getSysConfig();
        if (amountBigDecimal.compareTo(sysConfig.getWithdrawSingleMin()) < 0 || amountBigDecimal.compareTo(sysConfig.getWithdrawSingleMax()) > 0) {
            log.error("手机号：{} 单次提现量：{} 不在要求范围内", mobile, amount);
            throw new BusinessException(CodeEnum.WithDrawSingleLimitError);
        }
        BigDecimal total = Optional.ofNullable(userAssetDetailService.getCurrentDayTotalWithdraw(userId)).orElse(BigDecimal.ZERO);
        if (total.compareTo(sysConfig.getWithdrawDayLimit()) > 0) {
            log.error("手机号：{}超过当日提现限额");
            throw new BusinessException(CodeEnum.WithDrawDayLimitError);
        }
        if (!userAssetService.remainAsset(userId, amountBigDecimal)) {
            log.error("用户:{}余额不足", userId);
            throw new BusinessException(CodeEnum.RemainAssetError);
        }
        // 如果用户没有绑定以太坊地址，则给用户绑定以太坊地址
        if (StringUtils.isEmpty(ethAddress)) {
            log.info("手机号：{}绑定以太坊地址", mobile);
            ethAddress = getEthAddress();
        }
        // 发送提现请求
        long seq = new IdGen().nextId();
        WithdrawRequest request = new WithdrawRequest(mobile, amount, ethAddress, seq);
        WithdrawResponse response;
        try {
            response = request.execute();
        } catch (Exception ex) {
            // 捕获请求异常并设置网关日志
            sysLogService.createSysLog(mobile, request, SysLogTypeEnum.WITHDRAW.getValue(), SysLogStatusEnum.FAIL.getValue());
            // 抛出业务异常
            throw new BusinessException(CodeEnum.GateWayError);
        }
        // 判断结果
        Integer code = response.getCode();
        if (code == 0) {
            // 请求成功的网关日志
            sysLogService.createSysLog(mobile, request, SysLogTypeEnum.WITHDRAW.getValue(), SysLogStatusEnum.SUCCESS.getValue());
            // 提现成功后减少用户余额
            userAssetService.updateUserAsset(userId, amountBigDecimal.negate(), false);
            // 创建用户资产详情
            userAssetDetailService.insertUserDetailAsset(userId, null, amountBigDecimal, null, BigDecimal.ZERO, BigDecimal.ZERO, TransTypeEnum.NetOut.getValue(), null, String.valueOf(seq));
            return;
        }
        // 错误的请求日志
        log.error("网关响应失败:{}，信息:{}", code, response.getMsg());
        sysLogService.createSysLog(mobile, request, SysLogTypeEnum.WITHDRAW.getValue(), SysLogStatusEnum.FAIL.getValue());
        throw new BusinessException(CodeEnum.GateWayError);
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
    @Transactional(rollbackFor = Exception.class)
    public RechargeResponseData recharge(RechargeForm rechargeForm) {
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
            throw new RechargeException(RechargeStatusEnum.INVALID_ACCESS);
        }
        // 验证签名
        if (!StringUtils.equals(sign, rechargeForm.parameterSign())) {
            log.error("签名错误");
            throw new RechargeException(RechargeStatusEnum.INVALID_ACCESS);
        }
        // 验证合法性验证
        try {
            Long.parseLong(ts);
        } catch (NumberFormatException ex) {
            log.error("时间戳：{}格式不正确", ts);
            throw new RechargeException(RechargeStatusEnum.INVALID_ACCESS);
        }
        BigDecimal money;
        try {
            money = new BigDecimal(amount);
        } catch (NumberFormatException ex) {
            log.error("金额：{}格式不正确", amount);
            throw new RechargeException(RechargeStatusEnum.INVALID_ACCESS);
        }
        User user = userService.getByMobile(mobile);
        if (user == null) {
            log.error("pfc_account：{}不存在", mobile);
            throw new RechargeException(RechargeStatusEnum.UNKNOWN_ACCOUNT);
        }
        // 如果资产详情表中已经有对应的充值ID，则表示已经充值完成，不增加资产
        Long userId = user.getId();
        if (userAssetDetailService.getBySeq(seq) == 0) {
            // 更新资产
            userAssetService.updateUserAsset(userId, money, false);
            // 创建资产详情
            userAssetDetailService.insertUserDetailAsset(userId, null, money, null, BigDecimal.ZERO,
                    BigDecimal.ZERO, TransTypeEnum.NetIn.getValue(), null, seq);
        }
        // 返回数据
        RechargeResponseData data = new RechargeResponseData();
        data.setPfc_account(mobile);
        data.setEth_address(user.getEthAddress());
        return data;
    }

}
