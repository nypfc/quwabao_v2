package com.gedoumi.quwabao.team.service;

import com.gedoumi.quwabao.team.mapper.UserTreeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 用户上下级关系Service
 *
 * @author Minced
 */
@Service
public class UserTreeService {

    @Resource
    private UserTreeMapper userTreeMapper;

    /**
     * 创建用户上下级关系
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserTree(Long userId, Long parentId) {
        userTreeMapper.createUserTree(userId, parentId);
    }

}
