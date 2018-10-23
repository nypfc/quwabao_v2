package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.ResetPasswordForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.LoginTokenVO;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
    private UserAssetService userAssetService;
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
     * 获取用户资产
     *
     * @param userId 用户ID
     * @return 用户资产对象
     */
    public UserAsset getUserAsset(Long userId) {
        return userAssetService.getUserAsset(userId);
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
        String smsCode = CodeUtils.generateSMSCode();
        sysSmsService.sendSms(mobile, smsCode, SmsType.ResetPswd.getValue());
        return smsCode;
    }

    /**
     * 重置密码
     *
     * @param resetPasswordForm 重置密码表单
     * @return 令牌对象
     */
    public LoginTokenVO resetPswd(ResetPasswordForm resetPasswordForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取参数
        String mobile = resetPasswordForm.getMobile();
        String password = resetPasswordForm.getPassword();
        String validateCode = resetPasswordForm.getValidateCode();
        String smsCode = resetPasswordForm.getSmsCode();
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
        // 更新密码
        String token = UUID.randomUUID().toString();
        String encrypedPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));  // 密码加密
        userMapper.resetPassword(user.getId(), encrypedPassword, token, ContextUtil.getClientIp());
        // 更新缓存
        redisCache.setKeyValueData(token, user);
        // 封装令牌对象
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setUserName(user.getUsername());
        loginTokenVO.setMobilePhone(user.getMobilePhone());
        loginTokenVO.setToken(token);
        return loginTokenVO;
    }

}
