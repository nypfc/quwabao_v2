package com.gedoumi.quwabao.sys.request.response;

import lombok.Data;

/**
 * 短信返回对象
 *
 * @author Minced
 */
@Data
public class SMSResponse {

    /**
     * 成功响应吗
     */
    public static final String SUCCESS = "1";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回内容
     */
    private String content;

}
