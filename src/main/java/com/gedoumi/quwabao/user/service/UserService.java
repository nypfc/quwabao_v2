package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsTypeEnum;
import com.gedoumi.quwabao.common.enums.UserStatusEnum;
import com.gedoumi.quwabao.common.enums.UserTypeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.utils.PasswordUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.*;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static com.gedoumi.quwabao.common.constants.Constants.USER_PREFIX;


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
    private UserAssetService userAssetService;

    @Resource
    private UserTreeService userTreeService;

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
        return userMapper.selectByToken(token);
    }

    /**
     * 根据手机号获取用户
     *
     * @param mobile 手机号
     * @return 用户对象
     */
    public User getByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
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
        if (!sysSmsService.validateSms(mobile, smsCode, SmsTypeEnum.RESET_PASSWORD.getValue())) {
            log.error("手机号:{}验证码:{}错误", mobile, smsCode);
            throw new BusinessException(CodeEnum.SmsCodeError);
        }
        // 重置密码
        User user = Optional.ofNullable(userMapper.selectByMobile(mobile)).orElseThrow(() -> {
            log.error("手机号:{}未能查询到用户", mobile);
            return new BusinessException(CodeEnum.MobileNotExist);
        });
        String token = UUID.randomUUID().toString();
        user.setErrorCount(0);  // 错误次数重置
        user.setPassword(PasswordUtil.passwordEncrypt(mobile, password));
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
        String mobile = user.getMobilePhone();
        String password = user.getPassword();
        // 对比原密码是否正确
        String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone());
        String originalPassword = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getOriginalPassword(), salt);
        if (!StringUtils.equals(password, originalPassword)) {
            log.error("手机号:{}修改密码，原密码:{}与参数原密码:{}不相同", mobile, password, originalPassword);
            throw new BusinessException(CodeEnum.OrgPswdError);
        }
        // 修改密码
        String updatePassword = MD5EncryptUtil.md5Encrypy(updatePasswordForm.getPassword(), salt);
        user.setPassword(updatePassword);
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
        String username = updateUsernameForm.getUsername();
        // 从作用域中获取用户
        User user = ContextUtil.getUserFromRequest();
        // 判断用户名是否重复
        Integer count = userMapper.selectCountUsername(username);
        if (count > 0) {
            log.error("用户名:{}重复", username);
            throw new BusinessException(CodeEnum.NameError);
        }
        // 更新用户名
        user.setUsername(username);
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

    /**
     * 查询手机号是存在
     *
     * @param mobile 手机号
     * @return Boolean
     */
    public Boolean checkMobilePhone(String mobile) {
        return userMapper.selectCountMobile(mobile) != 0;
    }

    /**
     * 查询邀请码对应用户是否存在
     *
     * @param inviteCode 邀请码
     * @return Boolean
     */
    public Boolean checkInviteCode(String inviteCode) {
        return userMapper.selectCountInviteCode(inviteCode) != 0;
    }

    /**
     * 查询用户名对应用户是否存在
     *
     * @param username 用户名
     * @return Boolean
     */
    public Boolean checkUsername(String username) {
        return userMapper.selectCountUsername(username) != 0;
    }

    /**
     * 获取所有团队长ID
     *
     * @return 团队长用户ID集合
     */
    public List<Long> getTeamLeaderIds() {
        return userMapper.selectTeamLeaderIds(UserTypeEnum.LEADER.getValue());
    }

    /**
     * 更新手机号前验证手机号
     *
     * @param checkBeforUpdateForm 更新手机号前验证手机号表单
     */
    public void checkMobileBeforUpdate(CheckBeforUpdateForm checkBeforUpdateForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        String mobile = user.getMobilePhone();
        // 获取参数
        Integer type = Integer.parseInt(checkBeforUpdateForm.getType());
        String payPassword = PasswordUtil.passwordEncrypt(mobile, checkBeforUpdateForm.getPassword());
        String smsCode = checkBeforUpdateForm.getSmsCode();
        switch (type) {
            case 1:  // 支付密码验证
                if (StringUtils.isEmpty(payPassword)) {
                    log.error("支付密码验证方式：支付密码参数为空");
                    throw new BusinessException(CodeEnum.ParamError);
                }
                PasswordUtil.payPasswordValidate(user.getId(), user.getPayPassword(), payPassword);
                break;
            case 2:  // 短信验证
                if (StringUtils.isEmpty(smsCode)) {
                    log.error("短信验证方式：短信验证码参数为空");
                    throw new BusinessException(CodeEnum.ParamError);
                }
                // 短信验证码验证
                if (!sysSmsService.validateSms(mobile, smsCode, SmsTypeEnum.UPDATE_MOBILE.getValue())) {
                    log.error("手机号:{}验证码:{}错误", mobile, smsCode);
                    throw new BusinessException(CodeEnum.SmsCodeError);
                }
                break;
            default:
                log.error("type参数：{}有误", type);
                throw new BusinessException(CodeEnum.ParamError);
        }
    }

    /**
     * 更新手机号
     *
     * @param updateMobileForm 更新手机号表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMobile(UpdateMobileForm updateMobileForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取参数
        String newMobile = updateMobileForm.getNewMobile();
        String orgPassword = updateMobileForm.getOrgPassword();
        String smsCode = updateMobileForm.getSmsCode();
        // 参数验证
        if (StringUtils.equals(user.getMobilePhone(), newMobile)) {
            log.error("修改手机号与原手机号相同：{}", newMobile);
            throw new BusinessException(CodeEnum.MobileSame);
        }
        if (getByMobile(newMobile) != null) {
            log.error("修改的手机号已存在：{}", newMobile);
            throw new BusinessException(CodeEnum.MobileExist);
        }
        String encryptPassword = PasswordUtil.passwordEncrypt(user.getMobilePhone(), orgPassword);  // 原密码比对
        if (!StringUtils.equals(encryptPassword, user.getPassword())) {
            log.error("手机号:{}，密码:{}，密码不正确", user.getMobilePhone(), orgPassword);
            throw new BusinessException(CodeEnum.PasswordError);
        }
        // 短信验证码验证
        if (!sysSmsService.validateSms(newMobile, smsCode, SmsTypeEnum.UPDATE_MOBILE.getValue())) {
            log.error("手机号:{}验证码:{}错误", newMobile, smsCode);
            throw new BusinessException(CodeEnum.SmsCodeError);
        }
        // 更新手机号
        user.setMobilePhone(newMobile);
        user.setPassword(PasswordUtil.passwordEncrypt(newMobile, orgPassword));
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

    /**
     * 修改支付密码
     *
     * @param updatePayPasswordForm 修改支付密码表单
     */
    public void updatePayPassword(UpdatePayPasswordForm updatePayPasswordForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        Long userId = user.getId();
        // 获取参数
        String originalPayPassword = PasswordUtil.payPasswordEncrypt(userId, updatePayPasswordForm.getOriginalPassword());
        String newPayPassword = PasswordUtil.payPasswordEncrypt(userId, updatePayPasswordForm.getPassword());
        // 支付密码验证
        if (!StringUtils.equals(user.getPayPassword(), originalPayPassword)) {
            log.error("手机号：{}支付密码错误");
            throw new BusinessException(CodeEnum.PayPswdError);
        }
        // 修改支付密码
        user.setPayPassword(newPayPassword);
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

    /**
     * 重置支付密码
     *
     * @param resetPayPasswordForm 充值支付密码表单
     */
    public void resetPayPassword(ResetPayPasswordForm resetPayPasswordForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        String mobile = user.getMobilePhone();
        // 获取参数
        String payPassword = PasswordUtil.payPasswordEncrypt(user.getId(), resetPayPasswordForm.getPassword());
        String smsCode = resetPayPasswordForm.getSmsCode();
        // 短信验证码验证
        if (!sysSmsService.validateSms(mobile, smsCode, SmsTypeEnum.UPDATE_PAY_PASSWORD.getValue())) {
            log.error("手机号:{}验证码:{}错误", mobile, smsCode);
            throw new BusinessException(CodeEnum.SmsCodeError);
        }
        // 更新手机号
        user.setPayPassword(payPassword);
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
    }

    /**
     * 注册用户
     *
     * @param registerForm 注册表单
     * @return 注册成功的用户对象
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterForm registerForm) {
        // 获取参数
        String mobile = registerForm.getMobile();
        String smsCode = registerForm.getSmsCode();
        String username = registerForm.getUsername();
        String password = registerForm.getPassword();
        String inviteCode = registerForm.getInviteCode().toLowerCase();
        // 手机号验证
        if (checkMobilePhone(mobile)) {
            log.error("手机号:{}已经被注册", mobile);
            throw new BusinessException(CodeEnum.MobileExist);
        }
        // 短信验证码验证
        if (!sysSmsService.validateSms(mobile, smsCode, SmsTypeEnum.REGISTER.getValue())) {
            log.error("手机号:{}验证码:{}错误", mobile, smsCode);
            throw new BusinessException(CodeEnum.SmsCodeError);
        }
        // 邀请码验证
        if (!checkInviteCode(inviteCode)) {
            log.error("邀请码:{}对应用户不存在", inviteCode);
            throw new BusinessException(CodeEnum.InviteCodeError);
        }
        // 创建用户
        User user = new User();
        user.setMobilePhone(mobile);
        user.setPassword(PasswordUtil.passwordEncrypt(mobile, password));
        user.setUserStatus(UserStatusEnum.ENABLE.getValue());
        user.setLastLoginIp(ContextUtil.getClientIp());
        user.setToken(UUID.randomUUID().toString());
        user.setUserType(UserTypeEnum.NORMAL.getValue());
        user.setErrorCount(0);
        user.setRegInviteCode(inviteCode);
        user.setInviteCode(CodeUtils.generateCode());
        user.setSex(1);  // 冗余字段
        Date now = new Date();
        user.setLastLoginTime(now);
        user.setRegisterTime(now);
        user.setUpdateTime(now);
        while (checkInviteCode(user.getInviteCode()))
            user.setInviteCode(CodeUtils.generateCode());  // 设置邀请码，如果重复重新生成
        userMapper.insert(user);
        Long userId = user.getId();  // 创建完成的用户ID
        // 更新用户名
        User update = new User();
        update.setId(user.getId());
        if (StringUtils.isEmpty(username)) {
            do
                update.setUsername(USER_PREFIX + new Random().nextInt(999) + user.getId());  // 用户名为空设置默认用户名
            while (checkUsername(update.getUsername()));  // 如果重复继续设置用户名
        } else {
            if (checkUsername(username))
                throw new BusinessException(CodeEnum.NameError);
            update.setUsername(username);
        }
        userMapper.updateById(update);
        // 更新短信
        sysSmsService.updateSmsStatus(user.getMobilePhone());
        // 初始化用户资产
        userAssetService.createUserAsset(userId);
        // 创建用户上下级关系
        Long parentId = userMapper.selectIdByInviteCode(inviteCode);
        try {
            userTreeService.createUserTree(userId, parentId);
        } catch (DuplicateKeyException ex) {
            log.error("userId:{} parentId:{} 一个用户只能拥有一个上级", userId, parentId);
            throw new BusinessException(CodeEnum.BindInviteCodeError);
        }
        // 创建缓存
        redisCache.setKeyValueData(user.getToken(), user);
        return user;
    }

    /**
     * 根据用户对象更新用户
     *
     * @param user 用户对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(User user) {
        userMapper.updateById(user);
    }

}
