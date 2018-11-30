package com.gedoumi.quwabao.trans.dataobj.form;

import com.gedoumi.quwabao.common.utils.AesCBC;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

import static com.gedoumi.quwabao.common.constants.ApiConstants.APP_RECHARGE_CALLBACK;

/**
 * 充值回调表单
 */
@Data
public class RechargeForm {

    /**
     * PFC账号
     */
    private String pfc_account;

    /**
     * 资产名称
     */
    private String asset_name;

    /**
     * 充值金额
     */
    private String amount;

    /**
     * 时间戳
     */
    private String ts;

    /**
     * 充值唯一ID
     */
    private String seq;

    /**
     * 签名
     */
    private String sig;

    /**
     * 参数签名
     *
     * @return 签名字符串
     */
    public String parameterSign() {
        StringBuilder params = new StringBuilder(APP_RECHARGE_CALLBACK);
        params.append("amount")
                .append(getAmount())
                .append("asset_name")
                .append(getAsset_name())
                .append("pfc_account")
                .append(getPfc_account())
                .append("seq")
                .append(getSeq())
                .append("ts")
                .append(getTs());
        String sign = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/gateway.properties"));
            sign = AesCBC.encrypt(params.toString(), properties.getProperty("gateway.privateKey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

}
