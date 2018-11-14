package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.TeamLevel;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import com.gedoumi.quwabao.user.mapper.UserTeamExtMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    public UserTeamExt getTeamTotalRentMoney(Long userId) {
        return Optional.ofNullable(userTeamExtMapper.selectByUserId(userId)).orElseGet(() -> {
            UserTeamExt userTeamExt = new UserTeamExt();
            userTeamExt.setUserId(userId);
            userTeamExt.setTeamLevel(TeamLevel.LEVEL_0.getValue());
            userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
            userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
            userTeamExtMapper.insertSelective(userTeamExt);
            return userTeamExt;
        });
    }

}
