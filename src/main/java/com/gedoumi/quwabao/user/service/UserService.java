package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.ResetPasswordForm;
import com.gedoumi.quwabao.user.dataobj.form.UpdatePasswordForm;
import com.gedoumi.quwabao.user.dataobj.form.UpdateUsernameForm;
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
        return userMapper.queryByToken(token);
    }

    /**
     * 重置密码
     *
     * @param resetPasswordForm 重置密码表单
     * @return 重置密码后的用户对象
     */
    @Transactional(rollbackFor = Exception.class)
    public User resetPassword(ResetPasswordForm resetPasswordForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取参数
        String mobile = resetPasswordForm.getMobile();
        String password = resetPasswordForm.getPassword();
        String smsCode = resetPasswordForm.getSmsCode();
        // 短信验证
        Date date = Optional.ofNullable(sysSmsService.getSms(mobile, smsCode, SmsType.ResetPassword.getValue())).orElseThrow(() -> new BusinessException(CodeEnum.ValidateCodeExpire));
        long second = (new Date().getTime() - date.getTime()) / 1000;
        if (second >= Constants.EXPIRE_TIMES) {
            log.error("手机号:{}注册短信验证码:{}已过期", mobile, smsCode);
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        // 重置密码
        String token = UUID.randomUUID().toString();
        String encrypedPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));  // 密码加密
        user.setPassword(encrypedPassword);
        user.setToken(token);
        user.setLastLoginIp(ContextUtil.getClientIp());
        Date now = new Date();
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        userMapper.resetPassword(user);
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
        String orgPassword = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getOrgPswd(), salt);
        if (!StringUtils.equals(user.getPassword(), orgPassword)) {
            log.error("手机号:{}修改密码，原密码:{}与参数原密码:{}不相同", user.getMobilePhone(), user.getPassword(), orgPassword);
            throw new BusinessException(CodeEnum.OrgPswdError);
        }
        // 修改密码
        String pswd = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getPswd(), salt);
        user.setPassword(pswd);
        user.setUpdateTime(new Date());
        userMapper.updatePassword(user);
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
        if (userMapper.countByUsername(username) > 0) {
            log.error("用户名:{}重复", username);
            throw new BusinessException(CodeEnum.NameError);
        }
        // 更新用户名
        user.setUsername(username);
        userMapper.updateUsername(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

}
