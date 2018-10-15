package com.gedoumi.quwabao.user.mapper;

/**
 * 用户验证Mapper
 *
 * @author Minced
 */
public interface UserCheckMapper {

    /**
     * 根据手机号查询用户数量
     *
     * @param mobilePhone 手机号
     * @return 查询结果数量
     */
    Integer countByMobilePhone(String mobilePhone);

    /**
     * 根据邀请码查询用户数量
     *
     * @param regInviteCode 邀请码
     * @return 查询结果数量
     */
    Integer countByInviteCode(String regInviteCode);

    /**
     * 根据用户名查询用户数量
     *
     * @param username 用户名
     * @return 查询结果数量
     */
    Integer countByUsername(String username);

}
