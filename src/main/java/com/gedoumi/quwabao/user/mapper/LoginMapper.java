package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Minced
 */
@Mapper
public interface LoginMapper {

    /**
     * 根据手机号查询
     *
     * @param mobilePhone 手机号
     * @return 用户对象
     */
    User queryByMobilePhone(String mobilePhone);

    /**
     * 更新登录错误信息
     *
     * @param user 用户对象
     */
    void updateLoginErrorInfo(User user);

    /**
     * 更新登录信息
     *
     * @param user 用户对象
     */
    void updateLoginInfo(User user);

    /**
     * 更新退出信息
     *
     * @param user 用户对象
     */
    void updateLogoutInfo(User user);

}
