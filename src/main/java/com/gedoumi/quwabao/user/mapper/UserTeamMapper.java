package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户上下级Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserTeamMapper {

    /**
     * 根据上级用户ID查询下级用户集合
     *
     * @param parentId 上级用户ID
     * @return 用户集合
     */
    List<User> queryUserByParentId(Long parentId);

    /**
     * 根据上级用户ID查询下级用户ID集合
     *
     * @param parentId 上级用户ID集合
     * @return 下级用户ID集合
     */
    List<Long> queryUserIdsByParentId(List<Long> parentId);

    /**
     * 创建用户上下级
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    void createUserTree(Long userId, Long parentId);

}
