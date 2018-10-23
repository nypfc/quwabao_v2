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
     * 更新密码
     *
     * @param userId      用户ID
     * @param password    密码
     * @param token       令牌
     * @param lastLoginIp 最后登录IP
     */
    void resetPassword(Long userId, String password, String token, String lastLoginIp);

}
