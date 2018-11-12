package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface UserTreeMapper extends BaseMapper<UserTree> {

    /**
     * 根据上级用户ID查询下级用户集合
     *
     * @param parentId 上级用户ID
     * @return 用户集合
     */
    List<User> selectByParentId(Long parentId);

}
