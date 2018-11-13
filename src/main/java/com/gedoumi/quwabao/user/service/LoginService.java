package com.gedoumi.quwabao.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class LoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param loginForm 登录表单
     * @return 登录成功的用户对象
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public User login(LoginForm loginForm) {
        // 获取表单数据
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();
        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        String encryptedPassword = MD5EncryptUtil.md5Encrypy(password, salt);
        // 根据手机号获取用户并验证
        User user = Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, mobile)))
                .orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileNotExist);
        });
        if (user.getUserStatus() == UserStatusEnum.DISABLE.getValue()) {
            log.error("手机号:{}已经锁定", mobile);
            throw new BusinessException(CodeEnum.UserLocked);
        }
        if (user.getErrorCount() >= 3) {
            log.error("手机号:{}密码错误次数达到3次，需要重置密码", mobile);
            throw new BusinessException(CodeEnum.TooManyError);
        }
        if (!StringUtils.equals(encryptedPassword, user.getPassword())) {
            User update = new User();
            update.setId(user.getId());
            update.setErrorCount(user.getErrorCount() + 1);
            update.setErrorTime(new Date());
            userMapper.updateById(update);
            log.error("手机号:{}，密码:{}，密码不正确", mobile, password);
            throw new BusinessException(CodeEnum.PasswordError);
        }
        // 更新登录信息
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setErrorCount(0);  // 错误次数重置
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(ContextUtil.getClientIp());
        userMapper.updateById(user);
        // 缓存用户（失效时间1小时）
        redisCache.setExpireKeyValueData(token, user, 1L, TimeUnit.HOURS);
        return user;
    }

    /**
     * 用户退出
     *
     * @param token 令牌
     */
    public void logout(String token) {
        // 如果token和用户存在，更新updateTime字段，Token置空，删除缓存
        Optional.ofNullable(token).ifPresent(t -> Optional.ofNullable((User) redisCache.getKeyValueData(token)).ifPresent(user -> {
            log.info("手机号:{}退出登录", user.getMobilePhone());
            userMapper.update(user, new UpdateWrapper<User>().lambda()
                    .set(User::getToken, null)
                    .set(User::getUpdateTime, new Date())
                    .eq(User::getId, user.getId()));
            redisCache.deleteKeyValueData(token);
        }));
    }

}
