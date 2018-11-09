package com.gedoumi.quwabao.component;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
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
     * @return ResponseObject
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseObject businessException(BusinessException ex) {
        return new ResponseObject(ex.getCodeEnum());
    }

    /**
     * 请求方式不支持异常
     *
     * @param ex 异常
     * @return ResponseObject
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseObject httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuilder message = new StringBuilder("请求方式 [");
        message.append(ex.getMethod()).append("] 不支持, 需要 ").append(Arrays.toString(ex.getSupportedMethods()));
        return new ResponseObject(CodeEnum.SysError, message.toString());
    }

    /**
     * 参数验证
     *
     * @param ex 异常
     * @return ResponseObject
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseObject ConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseObject(CodeEnum.ParamError, ex.getMessage());
    }

    /**
     * 对象参数验证失败异常
     *
     * @param ex 异常
     * @return ResponseObject
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseObject methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("发现 ");
        sb.append(bindingResult.getErrorCount()).append(" 个参数错误; ");
        bindingResult.getFieldErrors().forEach(fieldError -> sb.append("错误的参数: ")
                .append(fieldError.getField())
                .append(", 原因: ")
                .append(fieldError.getDefaultMessage())
                .append("; "));
        String message = sb.toString();
        log.error(message);
        return new ResponseObject(CodeEnum.ParamError, message);
    }

    /**
     * 运行时异常
     *
     * @param ex 异常
     * @return ResponseObject
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseObject runtimeException(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseObject(CodeEnum.SysError);
    }

    /**
     * 异常
     *
     * @param ex 异常
     * @return 响应对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseObject exception(Exception ex) {
        ex.printStackTrace();
        return new ResponseObject(CodeEnum.SysError);
    }

}
