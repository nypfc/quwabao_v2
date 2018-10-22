package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.User;
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
     * @return 用户ID
     */
    Long createUser(User user);

}
