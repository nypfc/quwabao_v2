package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户关系Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserTreeMapper {

    /**
     * 根据上级用户ID查询下级用户集合
     *
     * @param parentId 上级用户ID
     * @return 用户集合
     */
    List<User> selectByParentId(Long parentId);

    /**
     * 根据子用户ID查询
     *
     * @param childId 子用户ID
     * @return 用户关系对象
     */
    UserTree selectByChildId(Long childId);

    /**
     * 创建用户关系
     *
     * @param userTree 用户关系对象
     * @return 数据库受影响行数
     */
    Integer insert(UserTree userTree);

}
