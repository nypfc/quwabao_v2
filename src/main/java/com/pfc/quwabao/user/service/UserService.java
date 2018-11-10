package com.pfc.quwabao.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfc.quwabao.common.enums.CodeEnum;
import com.pfc.quwabao.common.enums.SmsType;
import com.pfc.quwabao.common.exception.BusinessException;
import com.pfc.quwabao.common.utils.ContextUtil;
import com.pfc.quwabao.common.utils.MD5EncryptUtil;
import com.pfc.quwabao.component.RedisCache;
import com.pfc.quwabao.sys.dataobj.model.SysSms;
import com.pfc.quwabao.sys.service.SysSmsService;
import com.pfc.quwabao.user.dataobj.form.ResetPasswordForm;
import com.pfc.quwabao.user.dataobj.form.UpdatePasswordForm;
import com.pfc.quwabao.user.dataobj.form.UpdateUsernameForm;
import com.pfc.quwabao.user.dataobj.model.User;
import com.pfc.quwabao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


/**
 * 用户Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SysSmsService sysSmsService;

    @Resource
    private RedisCache redisCache;

    /**
     * 根据令牌获取用户
     *
     * @param token 令牌
     * @return 用户对象
     */
    public User getByToken(String token) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getToken, token));
    }

    /**
     * 重置密码
     *
     * @param resetPasswordForm 重置密码表单
     * @return 重置密码后的用户对象
     */
    @Transactional(rollbackFor = Exception.class)
    public User resetPassword(ResetPasswordForm resetPasswordForm) {
        // 获取参数
        String mobile = resetPasswordForm.getMobile();
        String password = resetPasswordForm.getPassword();
        String smsCode = resetPasswordForm.getSmsCode();
        // 短信验证
        String key = "sms:" + mobile;
        Optional.ofNullable((SysSms) redisCache.getKeyValueData(key))
                .filter(s -> s.getSmsType().equals(SmsType.ResetPassword.getValue()))
                .filter(s -> s.getCode().equals(smsCode)).orElseThrow(() -> {
            log.error("手机号:{}验证码:{}错误", mobile, smsCode);
            return new BusinessException(CodeEnum.SmsCodeError);
        });
        sysSmsService.updateSmsStatus(mobile);  // 短信置为失效
        redisCache.deleteKeyValueData(key);  // 删除缓存
        // 重置密码
        User user = Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, mobile))).orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileNotExist);
        });
        String token = UUID.randomUUID().toString();
        String encrypedPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));  // 密码加密
        user.setPassword(encrypedPassword);
        user.setToken(token);
        user.setLastLoginIp(ContextUtil.getClientIp());
        Date now = new Date();
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(token, user);
        return user;
    }

    /**
     * 修改密码
     *
     * @param updatePasswordForm 修改密码表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UpdatePasswordForm updatePasswordForm) {
        // 从作用域中获取用户
        User user = ContextUtil.getUserFromRequest();
        // 对比原密码是否正确
        String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone());
        String originalPassword = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getOriginalPassword(), salt);
        if (!StringUtils.equals(user.getPassword(), originalPassword)) {
            log.error("手机号:{}修改密码，原密码:{}与参数原密码:{}不相同", user.getMobilePhone(), user.getPassword(), originalPassword);
            throw new BusinessException(CodeEnum.OrgPswdError);
        }
        // 修改密码
        String password = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getOriginalPassword(), salt);
        user.setPassword(password);
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

    /**
     * 修改用户名
     *
     * @param updateUsernameForm 修改用户名表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUsername(UpdateUsernameForm updateUsernameForm) {
        // 获取参数
        String username = updateUsernameForm.getUserName();
        // 从作用域中获取用户
        User user = ContextUtil.getUserFromRequest();
        // 判断用户名是否重复
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count > 0) {
            log.error("用户名:{}重复", username);
            throw new BusinessException(CodeEnum.NameError);
        }
        // 更新用户名
        user.setUsername(username);
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

}
