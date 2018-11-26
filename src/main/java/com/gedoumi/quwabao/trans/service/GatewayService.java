package com.gedoumi.quwabao.trans.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SysLogStatusEnum;
import com.gedoumi.quwabao.common.enums.SysLogTypeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.trans.request.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    private RedisCache redisCache;

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
        String bindEthAddress = response.getData().getEth_address();
        if (code == 0 && StringUtils.isNotEmpty(bindEthAddress)) {
            // 请求成功的网关日志
            sysLogService.createSysLog(mobile, bind, SysLogTypeEnum.BIND_ETH_ADDRESS.getValue(), SysLogStatusEnum.SUCCESS.getValue());
            // 更新用户以太坊地址与更新缓存
            user.setEthAddress(bindEthAddress);
            userService.updateById(user);
            redisCache.setKeyValueData(user.getToken(), user);
            return bindEthAddress;
        } else {
            log.error("网关响应失败:{}，信息:{}", code, response.getMsg());
            // 错误的请求日志
            sysLogService.createSysLog(mobile, bind, SysLogTypeEnum.BIND_ETH_ADDRESS.getValue(), SysLogStatusEnum.FAIL.getValue());
            throw new BusinessException(CodeEnum.GateWayError);
        }
    }

}
