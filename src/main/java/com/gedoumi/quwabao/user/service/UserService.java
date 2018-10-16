package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.api.face.IDApiResponse;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.common.enums.UserType;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.*;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserImage;
import com.gedoumi.quwabao.user.dataobj.vo.ValidateUserVO;
import com.gedoumi.quwabao.user.mapper.UserImageMapper;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    private UserImageMapper userImageMapper;

    @Resource
    private UserCheckService userCheckService;

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
     * 创建用户
     *
     * @param mobile     手机号
     * @param password   密码
     * @param username   用户名
     * @param inviteCode 邀请人的邀请码
     * @return 创建后的用户对象
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public User createUser(String mobile, String password, String username, String inviteCode) {
        User user = new User();
        user.setMobilePhone(mobile);
        user.setPassword(MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile)));
        user.setUserStatus(UserStatus.Enable.getValue());
        Date now = new Date();
        user.setUpdateTime(now);
        user.setRegisterTime(now);
        user.setLastLoginTime(now);
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
        // 用户名不为空设置用户名
        if (StringUtils.isEmpty(username)) {
            int length = String.valueOf(user.getId()).length();
            length = length > 4 ? length : 4;
            String format = "%0" + length + "d";
            user.setUsername(USER_PREFIX + NumberUtil.randomInt(0, 999) + String.format(format, user.getId()));
        }
        userMapper.createUser(user);
        return user;
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
     * 用户验证
     *
     * @param validateUserVO
     * @param faceApiResponse
     */
    @Transactional
    public void validateUser(ValidateUserVO validateUserVO, IDApiResponse faceApiResponse) {
        if (faceApiResponse != null)
            log.info("validateUser faceApiResponse = {}", JsonUtil.objectToJson(faceApiResponse));
        Date now = new Date();
        Long userId = validateUserVO.getUserId();
        UserImage orgUserImage = userImageMapper.findByUserId(userId);
        if (orgUserImage != null) {
            orgUserImage.setUserImage(StringUtils.EMPTY);
            orgUserImage.setUpdateTime(now);
            orgUserImage.setMessage(faceApiResponse.getData().getMessage());
            orgUserImage.setValidateCode(faceApiResponse.getData().getCode());
            userImageMapper.save(orgUserImage);
        } else {
            UserImage userImage = new UserImage();
            userImage.setUserId(validateUserVO.getUserId());
            userImage.setUserImage(StringUtils.EMPTY);
            userImage.setCreateTime(now);
            userImage.setUpdateTime(now);
            userImage.setValidateCode(faceApiResponse.getData().getCode());
            userImage.setScore(StringUtils.EMPTY);
            userImage.setMessage(faceApiResponse.getData().getMessage());
            userImageMapper.save(userImage);
        }

        if (faceApiResponse != null) {
            User user = userMapper.findById(userId).get();
            user.setUpdateTime(now);
            user.setRealName(validateUserVO.getRealName());
            user.setIdCard(validateUserVO.getIdCard());
            if (faceApiResponse.isSucess()) {
                user.setValidateStatus(UserValidateStatus.Pass.getValue());
            } else {
                user.setValidateStatus(UserValidateStatus.UnPass.getValue());
            }

            userMapper.save(user);
        }

    }

}
