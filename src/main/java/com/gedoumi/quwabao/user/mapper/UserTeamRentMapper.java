package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserTeamRent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    Integer insert(UserTeamRent userTeamRent);

    /**
     * 批量添加用户团队业绩记录
     *
     * @param userTeamRents 记录集合
     */
    Integer insertBatch(List<UserTeamRent> userTeamRents);

}