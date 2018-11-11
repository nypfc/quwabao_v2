package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.RegisterForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import com.gedoumi.quwabao.user.mapper.UserRegisterMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
    private UserMapper userMapper;

    @Resource
    private SysSmsService sysSmsService;
    @Resource
    private UserCheckService userCheckService;
    @Resource
    private UserAssetService userAssetService;
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private RedisCache redisCache;

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
        String username = registerForm.getUserName();
        String password = registerForm.getPassword();
        String inviteCode = registerForm.getRegInviteCode().toLowerCase();

        // 手机号验证
        if (userCheckService.checkMobilePhone(mobile)) {
            log.error("手机号:{}已经被注册", mobile);
            throw new BusinessException(CodeEnum.MobileExist);
        }
        // 短信验证码验证
        String key = "sms:" + mobile;
        Optional.ofNullable((SysSms) redisCache.getKeyValueData(key))
                .filter(s -> s.getSmsType().equals(SmsType.Register.getValue()))
                .filter(s -> s.getCode().equals(smsCode)).orElseThrow(() -> {
            log.error("手机号:{}验证码:{}错误", mobile, smsCode);
            return new BusinessException(CodeEnum.SmsCodeError);
        });
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
        user.setRegInviteCode(inviteCode);
        user.setInviteCode(CodeUtils.generateCode());
        Date now = new Date();
        user.setLastLoginTime(now);
        user.setRegisterTime(now);
        user.setUpdateTime(now);
        while (userCheckService.checkInviteCode(user.getInviteCode()))
            user.setInviteCode(CodeUtils.generateCode());  // 设置邀请码，如果重复重新生成
        userRegisterMapper.createUser(user);
        Long userId = user.getId();  // 创建完成的用户ID

        // 更新用户名
        User update = new User();
        update.setId(user.getId());
        if (StringUtils.isEmpty(username)) {
            update.setUsername(USER_PREFIX + new Random().nextInt(999) + user.getId());  // 用户名为空设置默认用户名
            while (userCheckService.checkUsername(username))  // 如果重复继续设置用户名
                update.setUsername(USER_PREFIX + new Random().nextInt(999) + user.getId());
        } else {
            if (userCheckService.checkUsername(username))
                throw new BusinessException(CodeEnum.NameError);
            update.setUsername(username);
        }
        userMapper.updateById(update);

        // 更新短信
        sysSmsService.updateSmsStatus(user.getMobilePhone());

        // 创建用户资产
        userAssetService.createUserAsset(userId);

        // 创建用户上下级关系
        Long parentId = userRegisterMapper.queryIdByInviteCode(inviteCode);
        try {
            userTeamService.createUserTree(userId, parentId);
        } catch (DuplicateKeyException ex) {
            log.error("userId:{} parentId:{} 一个用户只能拥有一个上级", userId, parentId);
            throw new BusinessException(CodeEnum.BindInviteCodeError);
        }

        // 更新缓存
        redisCache.setKeyValueData(user.getToken(), user);
        return user;
    }

}
