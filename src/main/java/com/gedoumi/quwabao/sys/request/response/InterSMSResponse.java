package com.gedoumi.quwabao.sys.request.response;

import lombok.Data;

/**
 * 国际短信响应类
 *
 * @author Minced
 */
@Data
public class InterSMSResponse {

    /**
     * 响应吗
     */
    private String code;

    /**
     * 信息，如果成功则为空字符串
     */
    private String error;

    /**
     * 短信ID
     */
    private String msgid;

}
