package com.gedoumi.quwabao.trans.service;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.trans.request.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 请求API Service
 *
 * @author Minced
 */
@Service
public class GatewayService {

    /**
     * 绑定以太坊地址
     *
     * @return 以太坊地址
     */
    public String bindEthAddress() {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 如果用户已有以太坊地址，直接返回以太坊地址，否则先绑定地址
        String ethAddress = user.getEthAddress();
        if (StringUtils.isNotEmpty(ethAddress))
            return ethAddress;

        BindEthAddressRequest bind = new BindEthAddressRequest("13810060370");
        BindEthAddressResponse response = bind.execute();
        return "";
    }

}
