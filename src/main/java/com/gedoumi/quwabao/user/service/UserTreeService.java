package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserTree;
import com.gedoumi.quwabao.user.mapper.UserTreeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户关系Service
 *
 * @author Minced
 */
@Service
public class UserTreeService {

    @Resource
    private UserTreeMapper userTreeMapper;

    /**
     * 获取用户团队
     *
     * @param userId 用户ID
     * @return 团队用户集合
     */
    public List<User> getChildUser(Long userId) {
        return userTreeMapper.selectByParentId(userId);
    }

    /**
     * 获取下级用户的ID列表
     *
     * @param userId 用户ID
     * @return 用户ID集合
     */
    public List<Long> getChildIds(Long userId) {
        return userTreeMapper.selectIdsByParentId(userId);
    }

    /**
     * 根据子用户ID查询
     *
     * @param childId 子用户ID
     * @return 用户关系对象
     */
    public UserTree getByChildId(Long childId) {
        return userTreeMapper.selectByChildId(childId);
    }

    /**
     * 创建用户上下级关系
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserTree(Long userId, Long parentId) {
        UserTree userTree = new UserTree();
        userTree.setChildId(userId);
        userTree.setParentId(parentId);
        userTreeMapper.insert(userTree);
    }

}
