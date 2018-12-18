package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.PasswordTypeEnum;
import com.gedoumi.quwabao.common.enums.UserStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.component.RedisCache;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
    private UserService userService;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param loginForm 登录表单
     * @return 登录成功的用户对象
     */
    @Transactional(rollbackFor = Exception.class)
    public User login(LoginForm loginForm) {
        // 获取表单数据
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();
        // 根据手机号获取用户并验证
        User user = Optional.ofNullable(userService.getByMobile(mobile)).orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileNotExist);
        });
        if (user.getUserStatus() == UserStatusEnum.DISABLE.getValue()) {
            log.error("手机号:{}已经锁定", mobile);
            throw new BusinessException(CodeEnum.UserLocked);
        }
        if (!userService.passwordValidate(user, password, PasswordTypeEnum.LOGIN)) {
            log.error("手机号:{}，密码:{}，密码不正确", mobile, password);
            throw new BusinessException(CodeEnum.PasswordError);
        }
        // 更新登录信息
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setErrorCount(0);  // 错误次数重置
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(ContextUtil.getClientIp());
        userService.updateById(user);
        // 缓存用户
        redisCache.setKeyValueData(token, user);
        return user;
    }

    /**
     * 用户退出
     *
     * @param token 令牌
     */
    public void logout(String token) {
        // 如果token和用户存在，更新updateTime字段，Token置空，删除缓存
        if (StringUtils.isNotEmpty(token)) {
            Optional.ofNullable((User) redisCache.getKeyValueData(token)).ifPresent(user -> {
                log.info("手机号:{}退出登录", user.getMobilePhone());
                User update = new User();
                update.setId(user.getId());
                update.setToken(user.getToken());
                update.setUpdateTime(new Date());
                userService.updateById(update);
                redisCache.deleteKeyValueData(token);
            });
        }
    }

}
