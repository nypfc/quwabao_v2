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
     * 根据手机号查询
     *
     * @param mobilePhone 手机号
     * @return 用户对象
     */
    User queryByMobilePhone(String mobilePhone);

    User findByUsername(String username);

    User findByInviteCode(String inviteCode);

    User findByIdCard(String idCard);

    User findByUsernameAndUserStatus(String username, Integer userStatus);

    User findByMobilePhoneAndUserStatus(String mobile, Integer userStatus);

}
