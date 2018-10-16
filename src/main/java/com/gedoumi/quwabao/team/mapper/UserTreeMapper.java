package com.gedoumi.quwabao.team.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户上下级Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserTreeMapper {

    /**
     * 创建用户上下级
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    void createUserTree(Long userId, Long parentId);

}
