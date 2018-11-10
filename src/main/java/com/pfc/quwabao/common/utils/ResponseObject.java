package com.pfc.quwabao.common.utils;

import com.pfc.quwabao.common.enums.CodeEnum;
import lombok.Getter;

/**
 * 通用返回对象
 *
 * @param <T> 数据类型
 * @author Minced
 */
@Getter
public class ResponseObject<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 不带返回数据的构造方法
     */
    public ResponseObject() {
        this.code = CodeEnum.Success.getCode();
        this.message = CodeEnum.Success.getMessage();
    }

    /**
     * 带返回数据的构造方法
     *
     * @param data 数据
     */
    public ResponseObject(T data) {
        this.code = CodeEnum.Success.getCode();
        this.message = CodeEnum.Success.getMessage();
        this.data = data;
    }

    /**
     * 错误信息构造方法
     *
     * @param codeEnum 状态枚举
     */
    public ResponseObject(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    /**
     * 错误信息构造方法
     *
     * @param codeEnum 状态枚举
     * @param message  自定义信息
     */
    public ResponseObject(CodeEnum codeEnum, String message) {
        this.code = codeEnum.getCode();
        this.message = message;
    }

}
