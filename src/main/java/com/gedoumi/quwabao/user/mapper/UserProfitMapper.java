package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户收益Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserProfitMapper {

    /**
     * 根据用户ID查询用户收益列表
     *
     * @param userId 用户ID
     * @return 用户收益对象
     */
    List<UserProfit> selectByUserId(Long userId);

    /**
     * 创建用户收益
     *
     * @param userProfit 用户收益对象
     * @return 数据库受影响行数
     */
    Integer insert(UserProfit userProfit);

    /**
     * 根据ID更新用户收益
     *
     * @param userProfit 用户收益对象
     * @return 数据库受影响行数
     */
    Integer updateById(UserProfit userProfit);

}
