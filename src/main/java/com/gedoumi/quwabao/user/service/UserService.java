package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.utils.SmsUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.ResetPswdForm;
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
    private UserCheckService userCheckService;
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
        return Optional.ofNullable(userMapper.queryByToken(token)).orElseThrow(() -> {
            log.error("token:{}未查询到用户", token);
            return new BusinessException(CodeEnum.UnLogin);
        });
    }

    /**
     * 根据手机号获取用户
     *
     * @param mobile 手机号
     * @return 用户数据
     */
    public User getByMobilePhone(String mobile) {
        return userMapper.queryByMobilePhone(mobile);
    }

    /**
     * 更新登录错误信息
     *
     * @param user 用户对象
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateLoginErrorInfo(User user) {
        int errorCount = user.getErrorCount();
        errorCount++;
        userMapper.updateLoginErrorInfo(user.getId(), errorCount, user.getDeviceId());
    }

    /**
     * 更新登录信息
     *
     * @param user 用户对象
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateLoginInfo(User user) {
        userMapper.updateLoginInfo(user.getId(), ContextUtil.getClientIp(), user.getToken(), ContextUtil.getDeviceFromHead());
    }

    /**
     * 更新退出信息
     *
     * @param user 用户对象
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateLogoutInfo(User user) {
        userMapper.updateLogoutInfo(user.getId(), user.getToken());
    }

    /**
     * 获取上级用户ID
     *
     * @param inviteCode 邀请码
     * @return 上级用户ID
     */
    Long getParentUserId(String inviteCode) {
        return userMapper.queryUserIdByInviteCode(inviteCode);
    }

    /**
     * 获取重置密码用的短信验证码
     *
     * @param mobile 手机号
     * @param vCode  验证码
     * @return 验证码
     */
    public String getRpSmsCode(String mobile, String vCode) {
        // 手机号验证
        if (!userCheckService.checkMobilePhone(mobile)) {
            log.error("手机号:{}不存在", mobile);
            throw new BusinessException(CodeEnum.MobileError);
        }
        // 验证码验证
        String cacheValidateCode = (String) redisCache.getKeyValueData("vCode:" + mobile);
        if (cacheValidateCode == null) {
            log.error("未获取到缓存验证码，cacheValidateCode:null");
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        if (!StringUtils.equals(cacheValidateCode, vCode.toUpperCase())) {
            log.error("缓存的验证码:{}与参数验证码:{}不匹配", cacheValidateCode, vCode);
            throw new BusinessException(CodeEnum.ValidateCodeError);
        }
        // 当日短信上限验证
        if (sysSmsService.smsCurrentDayCount(mobile) >= Constants.SMS_DAY_COUNT) {
            log.error("手机号:{}当日验证码数量已达上限", mobile);
            throw new BusinessException(CodeEnum.SmsCountError);
        }
        // 发送短信
        String smsCode = SmsUtil.generateCode();
        SmsUtil.sendReg(mobile, smsCode);
        // 创建短信信息
        sysSmsService.createSysSms(mobile, smsCode, SmsType.ResetPswd.getValue());
        return smsCode;
    }

    /**
     * 重置密码
     *
     * @param resetPswdForm 重置密码表单
     * @return 令牌对象
     */
    public LoginToken resetPswd(ResetPswdForm resetPswdForm) {
        // 获取参数
        String mobile = resetPswdForm.getMobile();
        String password = resetPswdForm.getPassword();
        String validateCode = resetPswdForm.getValidateCode();
        String smsCode = resetPswdForm.getSmsCode();
        // 验证码验证
        String cacheValidateCode = (String) redisCache.getKeyValueData("vCode:" + mobile);
        if (cacheValidateCode == null) {
            log.error("未获取到缓存验证码，cacheValidateCode:null");
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        if (!StringUtils.equals(cacheValidateCode, validateCode.toUpperCase())) {
            log.error("缓存的验证码:{}与参数验证码:{}不匹配", cacheValidateCode, validateCode);
            throw new BusinessException(CodeEnum.ValidateCodeError);
        }
        // 短信验证
        Date date = Optional.ofNullable(sysSmsService.getSms(mobile, smsCode, SmsType.ResetPswd.getValue())).orElseThrow(() -> new BusinessException(CodeEnum.ValidateCodeExpire));
        long second = (new Date().getTime() - date.getTime()) / 1000;
        if (second >= Constants.EXPIRE_TIMES) {
            log.error("手机号:{}注册验证码:{}已过期", mobile, smsCode);
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        // 获取用户
        User user = Optional.ofNullable(userMapper.queryByMobilePhone(mobile)).orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileError);
        });
        // 更新密码
        String token = UUID.randomUUID().toString();
        String encrypedPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));  // 密码加密
        userMapper.resetPassword(user.getId(), encrypedPassword, token, ContextUtil.getClientIp());
        // 更新缓存
        redisCache.setKeyValueData(token, user);
        // 封装令牌对象
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(token);
        return loginToken;
    }

}
