package com.gedoumi.quwabao.component;

import com.gedoumi.quwabao.common.base.ErrorResponseObject;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 异常处理类
 *
 * @author Minced
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    /**
     * 项目内异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseObject businessException(BusinessException ex) {
        return new ErrorResponseObject(ex.getCodeEnum());
    }

    /**
     * 请求方式不支持异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponseObject httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuilder message = new StringBuilder("不支持的请求方式，当前请求方式为[");
        message.append(ex.getMethod()).append("]，支持的请求方式为").append(Arrays.toString(ex.getSupportedMethods()));
        return new ErrorResponseObject(message.toString());
    }

    /**
     * 参数验证失败异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseObject methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("有");
        sb.append(bindingResult.getErrorCount()).append("个参数出现错误；");
        bindingResult.getFieldErrors().forEach(fieldError -> sb.append("错误参数：")
                .append(fieldError.getField())
                .append("，原因：")
                .append(fieldError.getDefaultMessage())
                .append("；"));
        log.error(sb.toString());
        return new ErrorResponseObject(CodeEnum.ParamError);
    }

    /**
     * 运行时异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponseObject runtimeException(RuntimeException ex) {
        ex.printStackTrace();
        return new ErrorResponseObject(CodeEnum.SysError);
    }

    /**
     * 异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponseObject exception(Exception ex) {
        ex.printStackTrace();
        return new ErrorResponseObject(CodeEnum.SysError);
    }

}
