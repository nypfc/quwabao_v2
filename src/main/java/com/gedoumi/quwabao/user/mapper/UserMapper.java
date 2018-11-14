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
     * 根据手机号查询
     *
     * @param mobile 手机号
     * @return 用户对象
     */
    User selectByMobile(String mobile);

    /**
     * 根据令牌查询
     *
     * @param token 令牌
     * @return 用户对象
     */
    User selectByToken(String token);

    /**
     * 根据手机号查询用户数量
     *
     * @param mobile 手机号
     * @return 查询结果数量
     */
    Integer selectCountMobile(String mobile);

    /**
     * 根据用户名查询用户数量
     *
     * @param username 用户名
     * @return 查询结果数量
     */
    Integer selectCountUsername(String username);

    /**
     * 根据邀请码查询用户数量
     *
     * @param inviteCode 邀请码
     * @return 查询结果数量
     */
    Integer selectCountInviteCode(String inviteCode);

    /**
     * 根据邀请码查询用户ID
     *
     * @param inviteCode 邀请码
     * @return 用户ID
     */
    Long selectIdByInviteCode(String inviteCode);

    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 数据库受影响行数
     */
    Integer insertSelective(User user);

    /**
     * 根据主键更新用户
     *
     * @param user 用户对象
     * @return 数据库受影响行数
     */
    Integer updateByPrimaryKeySelective(User user);

}
