package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserTeamRent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户团队业绩记录Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserTeamRentMapper {

    /**
     * 添加记录
     *
     * @param userTeamRent 记录对象
     * @return 数据库受影响行数
     */
    int insert(UserTeamRent userTeamRent);

}