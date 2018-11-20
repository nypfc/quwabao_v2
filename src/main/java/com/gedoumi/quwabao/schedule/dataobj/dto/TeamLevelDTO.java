package com.gedoumi.quwabao.schedule.dataobj.dto;

import com.gedoumi.quwabao.common.enums.TeamLevelEnum;
import com.gedoumi.quwabao.user.dataobj.dto.UserAssetDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamRent;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 团队等级DTO
 *
 * @author Minced
 */
@Data
public class TeamLevelDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户团队等级
     */
    private Integer teamLevel = TeamLevelEnum.LEVEL_0.getValue();

    /**
     * 最大用户团杜等级
     */
    private Integer maxTeamLevel = TeamLevelEnum.LEVEL_0.getValue();

    /**
     * 累计静态收益
     */
    private BigDecimal totalStaticProfit = BigDecimal.ZERO;

    /**
     * 累计团队业绩（矿机总和）
     */
    private BigDecimal totalRentAsset = BigDecimal.ZERO;

    /**
     * 批量更新用户团队信息的集合
     */
    private List<UserTeamExt> userTeamExts = Lists.newArrayList();

    /**
     * 批量更新用户资产的集合
     */
    private List<UserAssetDTO> userAssets = Lists.newArrayList();

    /**
     * 批量更新用户收益的集合
     */
    private List<UserProfit> userProfits = Lists.newArrayList();

    /**
     * 批量添加用户资产详情的集合
     */
    private List<UserAssetDetail> userAssetDetails = Lists.newArrayList();

    /**
     * 批量添加用户每日团队业绩集合
     */
    private List<UserTeamRent> userTeamRents = Lists.newArrayList();

}
