package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.*;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.team.service.UserTreeService;
import com.gedoumi.quwabao.user.dataobj.form.RegisterForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.mapper.UserRegisterMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.gedoumi.quwabao.common.constants.Constants.USER_PREFIX;

/**
 * 用户注册Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserRegisterService {

    @Resource
    private UserRegisterMapper userRegisterMapper;

    @Resource
    private UserService userService;
    @Resource
    private SysSmsService sysSmsService;
    @Resource
    private UserCheckService userCheckService;
    @Resource
    private UserAssetService userAssetService;
    @Resource
    private UserTreeService userTreeService;
    @Resource
    private RedisCache redisCache;

    /**
     * 发送注册用短信验证码
     *
     * @param mobile 手机号
     * @param vCode  验证码
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void getRegSmsCode(String mobile, String vCode) {
        // 手机号验证
        if (userCheckService.checkMobilePhone(mobile)) {
            log.error("手机号:{}已被注册", mobile);
            throw new BusinessException(CodeEnum.MobileExist);
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
        sysSmsService.createSysSms(mobile, smsCode, SmsType.Register.getValue());
    }

    /**
     * 注册用户
     *
     * @param registerForm 注册表单
     * @return 令牌对象
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public LoginToken register(RegisterForm registerForm) {
        // 获取参数
        String mobile = registerForm.getMobile();
        String smsCode = registerForm.getSmsCode();
        String username = registerForm.getUserName();
        String password = registerForm.getPassword();
        String inviteCode = registerForm.getRegInviteCode().toLowerCase();

        // 手机号验证
        if (userCheckService.checkMobilePhone(mobile)) {
            log.error("手机号:{}已经被注册", mobile);
            throw new BusinessException(CodeEnum.MobileExist);
        }
        // 短信验证码验证
        Date date = Optional.ofNullable(sysSmsService.getSms(mobile, smsCode, SmsType.Register.getValue())).orElseThrow(() -> new BusinessException(CodeEnum.ValidateCodeExpire));
        long second = (new Date().getTime() - date.getTime()) / 1000;
        if (second >= Constants.EXPIRE_TIMES) {
            log.error("手机号:{}注册验证码:{}已过期", mobile, smsCode);
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        // 用户名验证
        if (StringUtils.isNotEmpty(username))
            if (userCheckService.checkUsername(username))
                throw new BusinessException(CodeEnum.NameError);
        // 邀请码验证
        if (!userCheckService.checkInviteCode(inviteCode)) {
            log.error("邀请码:{}对应用户不存在", inviteCode);
            throw new BusinessException(CodeEnum.InviteCodeError);
        }

        // 创建用户
        User user = new User();
        user.setMobilePhone(mobile);
        user.setPassword(MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile)));
        user.setUserStatus(UserStatus.Enable.getValue());
        user.setLastLoginIp(ContextUtil.getClientIp());
        user.setToken(UUID.randomUUID().toString());
        user.setUserType(UserType.Level_0.getValue());
        user.setErrorCount(0);
        user.setDeviceId(ContextUtil.getDeviceFromHead());
        user.setValidateStatus(UserValidateStatus.Init.getValue());
        user.setRegInviteCode(inviteCode);
        user.setInviteCode(CipherUtils.generateCode());
        // 设置邀请码，如果重复重新生成
        while (userCheckService.checkInviteCode(user.getInviteCode())) {
            user.setInviteCode(CipherUtils.generateCode());
        }
        // 用户名为空设置用户名
        if (StringUtils.isEmpty(username)) {
            int length = String.valueOf(user.getId()).length();
            length = length > 4 ? length : 4;
            String format = "%0" + length + "d";
            user.setUsername(USER_PREFIX + NumberUtil.randomInt(0, 999) + String.format(format, user.getId()));
        }
        userRegisterMapper.createUser(user);
        Long userId = user.getId();  // 创建完成的用户ID

        // 更新短信
        sysSmsService.updateSmsStatus(user.getMobilePhone());

        // 创建用户资产
        userAssetService.createUserAsset(userId);

        // 创建用户上下级关系
        Long parentId = userService.getParentUserId(inviteCode);
        try {
            userTreeService.createUserTree(userId, parentId);
        } catch (DuplicateKeyException ex) {
            log.error("userId:{} parentId:{} 一个用户只能拥有一个上级", userId, parentId);
            throw new BusinessException(CodeEnum.BindInviteCodeError);
        }

        // 设置缓存
        redisCache.setKeyValueData(user.getToken(), user);

        // 设置Token
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(user.getToken());

        return loginToken;
    }

    /**
     * 产生验证码
     *
     * @param mobile 手机号
     * @return 验证码
     */
    public String generateValidateCode(String mobile) {
        String validateCode = CipherUtils.generateValidateCode();
        // 设置2分钟失效的验证码
        redisCache.setExpireKeyValueData("reg:" + mobile, validateCode, 2L, TimeUnit.MINUTES);
        return validateCode;
    }

}
