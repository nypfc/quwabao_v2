package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.LoginTokenVO;
import com.gedoumi.quwabao.user.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

/**
 * 登录Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class LoginService {

    @Resource
    private LoginMapper loginMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param loginForm 登录表单
     * @return 登录成功的用户对象
     */
    @Transactional(noRollbackFor = {BusinessException.class}, rollbackFor = {Exception.class, RuntimeException.class})
    public User login(LoginForm loginForm) {
        // 获取表单数据
        String mobile = loginForm.getMobile();
        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        String pswd = MD5EncryptUtil.md5Encrypy(loginForm.getPassword(), salt);

        // 获取用户并验证
        User user = Optional.ofNullable(loginMapper.queryByMobilePhone(mobile)).orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileError);
        });
        if (user.getUserStatus() == UserStatus.Disable.getValue()) throw new BusinessException(CodeEnum.UserLocked);
        if (user.getErrorCount() > 3) throw new BusinessException(CodeEnum.LoginTimesError);
        if (!StringUtils.equals(pswd, user.getPassword())) {
            user.setDeviceId(ContextUtil.getDeviceFromHead());
            loginMapper.updateLoginErrorInfo(user);
            throw new BusinessException(CodeEnum.UserLoginError);
        }

        // 更新登录信息
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        loginMapper.updateLoginInfo(user);

        // 缓存用户
        redisCache.setKeyValueData(token, user);

        return user;
    }

    /**
     * 用户退出
     */
    public void logout() {
        String token = ContextUtil.getTokenFromHead();
        User user = (User) redisCache.getKeyValueData(token);
        if (token != null && user != null) {
            loginMapper.updateLogoutInfo(user);
            redisCache.deleteKeyValueData(token);
        }
    }

}
