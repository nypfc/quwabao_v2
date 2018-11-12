package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户团队Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserTeamExtMapper extends BaseMapper<UserTeamExt> {

    /**
     * 根据上级用户ID查询下级用户ID集合
     *
     * @param parentId 上级用户ID集合
     * @return 下级用户ID集合
     */
    List<Long> queryUserIdsByParentId(List<Long> parentId);

}
