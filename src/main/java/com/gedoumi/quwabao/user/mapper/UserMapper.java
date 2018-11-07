package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

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
     * 更新用户名
     *
     * @param user 用户对象
     */
    void updateUsername(User user);

}
