package com.gedoumi.quwabao.component;

import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实名验证拦截器
 *
 * @author Minced
 */
@Slf4j
public class RealNameInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取Request作用域中的用户数据
        User user = (User) request.getAttribute(Constants.API_USER_KEY);
        if (user.getValidateStatus() == null || user.getValidateStatus() != UserValidateStatus.Pass.getValue()) {
            log.error("{}用户还未实名认证", user.getMobilePhone());
            throw new BusinessException(CodeEnum.ValidateError);
        }
        return true;
    }

}
