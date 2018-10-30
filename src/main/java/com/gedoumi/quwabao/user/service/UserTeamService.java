package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.mapper.UserTeamMapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 用户上下级关系Service
 *
 * @author Minced
 */
@Service
public class UserTeamService {

    @Resource
    private UserTeamMapper userTeamMapper;

    @Resource
    private UserRentService userRentService;

    /**
     * 获取用户团队
     *
     * @param userId 用户ID
     * @return 团队用户集合
     */
    public List<User> getChildUser(Long userId) {
        return userTeamMapper.queryUserByParentId(userId);
    }

    /**
     * 获取用户团队的租用矿机价格的总和（不包含自己）
     *
     * @param userId 用户ID
     * @return 租用的矿机价格的总和
     */
    public BigDecimal getTeamTotalRentMoney(Long userId) {
        // 获取递归调用的所有下级用户ID（不包含自己）的ID集合
        List<Long> childIds = getUsers(Lists.newArrayList(userId)).stream()
                .filter(childId -> !childId.equals(userId)).collect(Collectors.toList());
        // 若排除自己本身后集合为空，则直接返回0
        if (CollectionUtils.isEmpty(childIds))
            return BigDecimal.ZERO;
        // 如果查询结果为空（没有矿机），返回0
        return Optional.ofNullable(userRentService.getTotalRentAsset(childIds)).orElse(BigDecimal.ZERO);
    }

    /**
     * 递归调用
     *
     * @param userIds 用户ID集合
     * @return 下级用户ID集合
     */
    private List<Long> getUsers(List<Long> userIds) {
        // 如果下级用户不为空，继续递归调用，添加到新集合中，否者直接返回下级用户ID集合
        List<Long> childIds = userTeamMapper.queryUserIdsByParentId(userIds);
        if (!CollectionUtils.isEmpty(childIds))
            childIds.addAll(getUsers(childIds));
        return childIds;
    }

    /**
     * 创建用户上下级关系
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserTree(Long userId, Long parentId) {
        userTeamMapper.createUserTree(userId, parentId);
    }

}
