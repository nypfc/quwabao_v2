package com.gedoumi.quwabao.common.base;

import lombok.Data;

/**
 * 错误响应对象
 *
 * @author Minced
 */
@Data
public class ErrorResponseObject {

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 创建失败响应数据
     *
     * @param message 错误信息
     * @return 响应数据对象
     */
    public static ErrorResponseObject setErrorResponse(String message) {
        ErrorResponseObject responseObject = new ErrorResponseObject();
        responseObject.setCode("0001");
        responseObject.setMessage(message);
        return responseObject;
    }

}
