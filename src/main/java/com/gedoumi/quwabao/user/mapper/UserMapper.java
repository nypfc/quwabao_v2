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
     * 重置密码
     *
     * @param user 用户对象
     */
    void resetPassword(User user);

    /**
     * 更新密码
     *
     * @param user 用户对象
     */
    void updatePassword(User user);

    /**
     * 查询用户名是否重复
     *
     * @param username 用户名
     * @return 查询结果数量
     */
    Integer countByUsername(String username);

    /**
     * 更新用户名
     *
     * @param user 用户对象
     */
    void updateUsername(User user);

}
