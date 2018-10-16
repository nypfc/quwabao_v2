package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 登录Service
 *
 * @author Minced
 */
@Service
public class LoginService {

    @Resource
    private UserService userService;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param loginForm 登录表单
     * @return 令牌对象
     */
    @Transactional(noRollbackFor = {BusinessException.class}, rollbackFor = {Exception.class, RuntimeException.class})
    public LoginToken login(LoginForm loginForm) {
        // 获取表单数据
        String mobile = loginForm.getMobile();
        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        String pswd = MD5EncryptUtil.md5Encrypy(loginForm.getPassword(), salt);
        // 获取用户并验证
        User user = userService.getByMobilePhone(mobile);
        if (user == null) throw new BusinessException(CodeEnum.UserLoginError);
        if (user.getUserStatus() == UserStatus.Disable.getValue()) throw new BusinessException(CodeEnum.UserLocked);
        if (user.getErrorCount() > 3) throw new BusinessException(CodeEnum.LoginTimesError);
        if (!StringUtils.equals(pswd, user.getPassword())) {
            user.setDeviceId(ContextUtil.getDeviceFromHead());
            userService.updateLoginErrorInfo(user);
            throw new BusinessException(CodeEnum.UserLoginError);
        }
        // 缓存用户
        String token = UUID.randomUUID().toString();
        redisCache.setKeyValueData(token, user);
        // 更新登录信息
        userService.updateLoginInfo(user);
        // 封装令牌对象
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(token);
        return loginToken;
    }

    /**
     * 用户退出
     */
    public void logout() {
        String token = ContextUtil.getTokenFromHead();
        User user = (User) redisCache.getKeyValueData(token);
        if (user != null) {
            userService.updateLogoutInfo(user);
            redisCache.deleteKeyValueData(token);
        }
    }

}
