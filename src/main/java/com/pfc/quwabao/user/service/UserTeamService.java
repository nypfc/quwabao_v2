package com.pfc.quwabao.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfc.quwabao.common.enums.TeamLevel;
import com.pfc.quwabao.user.dataobj.model.User;
import com.pfc.quwabao.user.dataobj.model.UserTeamExt;
import com.pfc.quwabao.user.mapper.UserTeamExtMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * 用户上下级关系Service
 *
 * @author Minced
 */
@Service
public class UserTeamService {

    @Resource
    private UserTeamExtMapper userTeamExtMapper;

    /**
     * 获取用户团队
     *
     * @param userId 用户ID
     * @return 团队用户集合
     */
    public List<User> getChildUser(Long userId) {
        return userTeamExtMapper.queryUserByParentId(userId);
    }

    /**
     * 获取团队业绩
     *
     * @param userId 用户ID
     * @return 团队业绩
     */
    public UserTeamExt getTeamTotalRentMoney(Long userId) {
        return Optional.ofNullable(userTeamExtMapper.selectOne(new LambdaQueryWrapper<UserTeamExt>().eq(UserTeamExt::getUserId, userId))).orElseGet(() -> {
            UserTeamExt userTeamExt = new UserTeamExt();
            userTeamExt.setUserId(userId);
            userTeamExt.setTeamLevel(TeamLevel.LEVEL_0.getValue());
            userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
            userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
            userTeamExtMapper.insert(userTeamExt);
            return userTeamExt;
        });
    }

    /**
     * 创建用户上下级关系
     *
     * @param userId   用户ID
     * @param parentId 上级用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserTree(Long userId, Long parentId) {
        userTeamExtMapper.createUserTree(userId, parentId);
    }

}
