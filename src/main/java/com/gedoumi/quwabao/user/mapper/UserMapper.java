package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserMapper {

    /**
     * 根据令牌查询
     *
     * @param token 令牌
     * @return 用户对象
     */
    User queryByToken(String token);

    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 用户ID
     */
    Long createUser(User user);

    /**
     * 根据手机号查询
     *
     * @param mobilePhone 手机号
     * @return 用户对象
     */
    User queryByMobilePhone(String mobilePhone);

    /**
     * 根据邀请码查询用户ID
     *
     * @param inviteCode 邀请码
     * @return 用户ID
     */
    Long queryUserIdByInviteCode(String inviteCode);

    /**
     * 更新登录错误信息
     *
     * @param userId     用户ID
     * @param errorCount 错误次数
     * @param deviceId   设备ID
     */
    void updateLoginErrorInfo(Long userId, Integer errorCount, String deviceId);

    /**
     * 更新登录信息
     *
     * @param userId      用户Id
     * @param lastLoginIp 最后登录IP
     * @param token       令牌
     * @param deviceId    设备ID
     */
    void updateLoginInfo(Long userId, String lastLoginIp, String token, String deviceId);

    /**
     * 更新退出信息
     *
     * @param userId 用户ID
     * @param token  令牌
     */
    void updateLogoutInfo(Long userId, String token);

    /**
     * 根据身份证号查询
     *
     * @param idCard 身份证号
     * @return 用户对象
     */
    User queryByIdCard(String idCard);

}
