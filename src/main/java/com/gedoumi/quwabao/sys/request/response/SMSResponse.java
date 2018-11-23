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
     * 返回码
     */
    private String code;

    /**
     * 返回内容
     */
    private String content;

}
