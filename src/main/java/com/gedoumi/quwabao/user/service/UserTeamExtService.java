package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.TeamLevelEnum;
import com.gedoumi.quwabao.common.enums.TeamLevelManualEnum;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import com.gedoumi.quwabao.user.mapper.UserTeamExtMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * 用户上下级关系Service
 *
 * @author Minced
 */
@Service
public class UserTeamExtService {

    @Resource
    private UserTeamExtMapper userTeamExtMapper;

    /**
     * 获取团队业绩
     *
     * @param userId 用户ID
     * @return 团队业绩
     */
    public UserTeamExt getUserTeamExt(Long userId) {
        return Optional.ofNullable(userTeamExtMapper.selectByUserId(userId)).orElseGet(() -> insertUserTeamExt(userId));
    }

    /**
     * 初始化用户团队信息
     *
     * @param userId 用户ID
     * @return 用户团队信息对象
     */
    public UserTeamExt insertUserTeamExt(Long userId) {
        Date now = new Date();
        UserTeamExt userTeamExt = new UserTeamExt();
        userTeamExt.setUserId(userId);
        userTeamExt.setTeamLevel(TeamLevelEnum.LEVEL_0.getValue());
        userTeamExt.setManualTeamLevel(TeamLevelManualEnum.AUTO.getValue());
        userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
        userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
        userTeamExt.setCreateTime(now);
        userTeamExt.setUpdateTime(now);
        userTeamExtMapper.insert(userTeamExt);
        return userTeamExt;
    }

    /**
     * 批量更新用户团队信息
     *
     * @param userTeamExts 用户团队信息集合
     */
    public void updateBatch(List<UserTeamExt> userTeamExts) {
        userTeamExtMapper.updateBatch(userTeamExts);
    }

}
