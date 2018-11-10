package com.pfc.quwabao.user.mapper;

import com.pfc.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserRegisterMapper {

    /**
     * 创建用户
     *
     * @param user 用户对象
     */
    void createUser(User user);

    /**
     * 根据邀请码查询用户ID
     *
     * @param inviteCode 邀请码
     * @return 用户ID
     */
    Long queryIdByInviteCode(String inviteCode);

}
