package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.enums.TeamLevelEnum;
import com.gedoumi.quwabao.common.enums.TeamLevelManualEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.schedule.dataobj.dto.TeamLevelDTO;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.service.SysConfigService;
import com.gedoumi.quwabao.user.dataobj.dto.UserAssetDTO;
import com.gedoumi.quwabao.user.dataobj.dto.UserProfitDTO;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentDTO;
import com.gedoumi.quwabao.user.dataobj.dto.UserTeamDTO;
import com.gedoumi.quwabao.user.dataobj.model.*;
import com.gedoumi.quwabao.user.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.gedoumi.quwabao.common.constants.Constants.*;

/**
 * 定时任务执行
 *
 * @author Minced
 */
@Slf4j
@Service
public class RunScheduleTask {

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private UserRentService userRentService;

    @Resource
    private UserProfitService userProfitService;

    @Resource
    private UserTreeService userTreeService;

    @Resource
    private UserTeamExtService userTeamExtService;

    @Resource
    private UserTeamRentService userTeamRentService;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 计算挖矿收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void digTask() {
        // 查询所有正在使用中的矿机
        List<UserRentDTO> rentDTOS = userRentService.getAllUserActiveRent();
        if (CollectionUtils.isEmpty(rentDTOS)) {
            log.info("没有符合条件的矿机");
            return;
        }
        log.info("矿机数量:{}", rentDTOS.size());
        // 当前日期
        Date now = new Date();
        // 批量更新的集合
        ArrayList<UserRent> updateUserRents = Lists.newArrayList();
        ArrayList<UserAssetDetail> insertUserAssetDetails = Lists.newArrayList();
        HashMap<Long, BigDecimal> map = Maps.newHashMap();
        ArrayList<UserAssetDTO> updateUserAssets = Lists.newArrayList();
        ArrayList<UserProfit> insertUserProfits = Lists.newArrayList();
        // 遍历用户矿机集合
        rentDTOS.forEach(rentDTO -> {
            BigDecimal rentAsset = rentDTO.getRentAsset();  // 矿机价格
            BigDecimal profitDay = rentDTO.getProfitDay();  // 当前每日产币量
            BigDecimal alreadyDig = rentDTO.getAlreadyDig();  // 已经获得的币量
            BigDecimal totalAsset = rentDTO.getTotalAsset();  // 总币量
            BigDecimal remainAsset = totalAsset.subtract(alreadyDig);  // 剩余币量
            // 用户矿机信息
            UserRent userRent = new UserRent();
            userRent.setId(rentDTO.getId());
            if (remainAsset.compareTo(profitDay) < 0) {
                // 如果剩余币量不足每日的产币量，则直接结束矿机
                userRent.setExpireDate(now);
                userRent.setRentStatus(RentStatusEnum.STOP.getValue());
                userRent.setUpdateTime(now);
                updateUserRents.add(userRent);
            } else {
                userRent.setLastDig(profitDay);
                userRent.setAlreadyDig(alreadyDig.add(profitDay));
                userRent.setUpdateTime(now);
                // 如果已挖矿量与总收益相同，说明正好挖完，将矿机置为停用
                if (userRent.getAlreadyDig().compareTo(totalAsset) == 0)
                    userRent.setRentStatus(RentStatusEnum.STOP.getValue());
                updateUserRents.add(userRent);

                // 累加用户矿机总收益
                Long userId = rentDTO.getUserId();
                map.put(userId, map.getOrDefault(userId, BigDecimal.ZERO).add(profitDay));

                // 向用户资产详情集合中增加数据
                BigDecimal totalDay = totalAsset.divide(profitDay, 5, BigDecimal.ROUND_DOWN);  // 计算总共需要多少天挖完
                BigDecimal profit = totalAsset.subtract(rentAsset).divide(totalDay, 5, BigDecimal.ROUND_DOWN);  // 计算带本金的收益
                insertUserAssetDetails.add(createUserAssetDetail(rentDTO.getUserId(), profitDay,
                        rentDTO.getId(), profit, profitDay, TransTypeEnum.Profit.getValue(), null, now));
            }
        });
        // 遍历用户总收益
        map.forEach((userId, totalProfit) -> {
            // 向用户资产集合中增加数据
            updateUserAssets.add(createUserAsset(userId, totalProfit, now));
            // 向用户收益集合中增加数据
            insertUserProfits.add(createUserProfit(userId, totalProfit, null, null, totalProfit, now));
        });
        // 批量更新矿机
        if (updateUserRents.size() > 0) userRentService.updateBatch(updateUserRents);
        // 批量更新用户资产
        if (updateUserAssets.size() > 0) userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户收益
        if (insertUserProfits.size() > 0) userProfitService.insertBatch(insertUserProfits);
        // 批量添加用户资产详情
        if (insertUserAssetDetails.size() > 0) userAssetDetailService.insertBatch(insertUserAssetDetails);
    }

    /**
     * 计算推荐人收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask() {
        // 当前日期
        Date now = new Date();
        // 批量更新的集合
        ArrayList<UserAssetDetail> insertUserAssetDetails = Lists.newArrayList();
        HashMap<Long, BigDecimal> map = Maps.newHashMap();
        ArrayList<UserAssetDTO> updateUserAssets = Lists.newArrayList();
        ArrayList<UserProfit> updateUserProfits = Lists.newArrayList();
        // 获取系统配置
        SysConfig sysConfig = sysConfigService.getSysConfig();
        // 遍历用户
        List<UserProfit> userProfits = userProfitService.getCurrentDayUserProfits(DATE_FORMAT.format(now));
        userProfits.forEach(up -> {
            // 当前用户ID
            Long userId = up.getUserId();
            // 获取当前用户的整个链条
            ArrayList<Long> list = Lists.newArrayList();
            list.add(userId);
            UserTree userTree = userTreeService.getByChildId(userId);
            while (userTree != null) {
                Long parentId = userTree.getParentId();
                list.add(parentId);
                userTree = userTreeService.getByChildId(parentId);
            }
            // 如果链条只有用户自己，跳过循环
            int size = list.size();
            if (size == 1)
                return;
            // 当前用户总静态收益（所有激活的矿机价格的每日收益）* 0.2（0.2为可变值）
            BigDecimal staticReward = up.getStaticProfit().multiply(sysConfig.getStaticProfit()).setScale(5, BigDecimal.ROUND_DOWN);
            // 遍历链条
            for (int i = 0; i < size; i++) {
                // 第0层为自身，自身不获取收益，从第一层开始获取收益
                // 超过11层则停止收益
                if (i - 1 >= 0 && i <= 11) {
                    Long parentId = list.get(i);
                    // 动态收益 = 当前用户静态收益 * 0.2（0.2位可变值）* 0.5 (i - 1)次方（0.5为可变值）
                    BigDecimal count = sysConfig.getDynamicProfit().pow(i - 1).setScale(5, BigDecimal.ROUND_DOWN);
                    BigDecimal dynamicReward = staticReward.multiply(count).setScale(5, BigDecimal.ROUND_DOWN);
                    // 累加用户的动态收益
                    map.put(parentId, map.getOrDefault(parentId, BigDecimal.ZERO).add(dynamicReward));
                    // 资产详情按正常收益储存，用于数据记录
                    // 如果为收益0，不添加数据
                    if (dynamicReward.compareTo(BigDecimal.ZERO) != 0) {
                        insertUserAssetDetails.add(createUserAssetDetail(parentId, dynamicReward, null,
                                BigDecimal.ZERO, BigDecimal.ZERO, TransTypeEnum.Reward.getValue(), userId, now));
                    }
                }
            }
        });
        // 遍历总收益集合
        map.forEach((userId, totalDynamicProfit) -> {
            UserProfitDTO dto = Optional.ofNullable(userProfitService.getCurrentDayUserProfit(userId, DATE_FORMAT.format(now)))
                    .orElse(new UserProfitDTO());
            // 获取收益数据
            BigDecimal staticProfit = dto.getStaticProfit() == null ? BigDecimal.ZERO : dto.getStaticProfit();
            BigDecimal totalRentAsset = dto.getTotalRentAsset() == null ? BigDecimal.ZERO : dto.getTotalRentAsset();
            // 计算新的总收益
            BigDecimal totalProfit = staticProfit.add(totalDynamicProfit);
            // 如果总收益大于已激活的矿机价格，则将动态收益多余的部分减去
            if (totalProfit.compareTo(totalRentAsset) > 0) {
                totalProfit = totalRentAsset;
                totalDynamicProfit = totalProfit.subtract(totalRentAsset).setScale(5, BigDecimal.ROUND_DOWN);
            }
            // 向用户资产集合中增加数据
            updateUserAssets.add(createUserAsset(userId, totalDynamicProfit, now));
            // 向用户收益集合中增加数据
            updateUserProfits.add(createUserProfit(userId, staticProfit, totalDynamicProfit, null, totalProfit, null));
        });
        // 批量更新用户资产
        if (updateUserAssets.size() > 0) userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户收益
        if (updateUserProfits.size() > 0) userProfitService.updateBatch(updateUserProfits);
        // 批量添加用户资产详情
        if (insertUserAssetDetails.size() > 0) userAssetDetailService.insertBatch(insertUserAssetDetails);
    }

    /**
     * 计算俱乐部收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void clubRewardTask() {
        // 当前日期
        Date now = new Date();
        List<UserAssetDTO> updateUserAssets = Lists.newArrayList();
        List<UserProfit> updateUserProfits = Lists.newArrayList();
        List<UserAssetDetail> insertUserAssetDetails = Lists.newArrayList();
        List<UserTeamRent> insertUserTeamRents = Lists.newArrayList();
        List<UserTeamExt> updateUserTeamExts = Lists.newArrayList();

        // 遍历当日收益，递归用户树
        List<TeamLevelDTO> teamDTOs = Lists.newArrayList();
        userService.getTeamLeaderIds().forEach(userId -> teamDTOs.add(teamReward(userId, now)));
        // 累加集合
        teamDTOs.forEach(dto -> {
            updateUserAssets.addAll(dto.getUserAssets());
            updateUserProfits.addAll(dto.getUserProfits());
            insertUserAssetDetails.addAll(dto.getUserAssetDetails());
            insertUserTeamRents.addAll(dto.getUserTeamRents());
            updateUserTeamExts.addAll(dto.getUserTeamExts());
        });
        // 获取所有皇家用户ID并进行遍历
        List<UserTeamExt> topTeam = updateUserTeamExts.stream()
                .filter(ute -> ute.getTeamLevel().equals(TeamLevelEnum.LEVEL_4.getValue()))
                .collect(Collectors.toList());
        int count = topTeam.size();
        if (count > 0) {
            // 所有皇家用户平分当天所有产币量的1%
            BigDecimal totalStaticProfit = Optional.ofNullable(userProfitService.getCurrentDayTotalStaticProfit(DATE_FORMAT.format(now)))
                    .orElse(BigDecimal.ZERO);
            BigDecimal clubProfit = totalStaticProfit.multiply(TEAM_LEVEL4_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN)
                    .divide(new BigDecimal(count), 5, BigDecimal.ROUND_DOWN);
            for (UserTeamExt userTeamExt : topTeam) {
                Long userId = userTeamExt.getUserId();
                // 如果当天总收益超过了正在激活矿机的总价格，总收益置为总矿机价格，俱乐部多余部分减去，剩余的部分为实际的俱乐部收益
                UserProfitDTO profitDTO = userProfitService.getCurrentDayUserProfit(userId, DATE_FORMAT.format(now));
                BigDecimal rentAsset = profitDTO.getTotalRentAsset();
                // 添加俱乐部收益
                BigDecimal staticProfit = profitDTO.getStaticProfit();
                BigDecimal dynamicProfit = profitDTO.getDynamicProfit();
                BigDecimal total = staticProfit.add(dynamicProfit).add(clubProfit);
                if (total.compareTo(rentAsset) > 0) {
                    clubProfit = rentAsset.subtract(staticProfit).subtract(dynamicProfit).setScale(5, BigDecimal.ROUND_DOWN);
                }
                updateUserAssets.add(createUserAsset(userId, clubProfit, now));
                updateUserProfits.add(createUserProfit(userId, staticProfit, dynamicProfit, clubProfit, rentAsset, now));
                insertUserAssetDetails.add(createUserAssetDetail(userId, clubProfit,
                        null, null, null, TransTypeEnum.Reward.getValue(), null, now));
            }
        }
        // 批量更新用户资产
        if (updateUserAssets.size() > 0) userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户收益
        if (updateUserProfits.size() > 0) userProfitService.updateBatch(updateUserProfits);
        // 批量添加用户资产详情
        if (insertUserAssetDetails.size() > 0) userAssetDetailService.insertBatch(insertUserAssetDetails);
        // 批量添加团队业绩记录
        if (updateUserTeamExts.size() > 0) userTeamExtService.updateBatch(updateUserTeamExts);
        // 批量更新团队信息
        if (insertUserTeamRents.size() > 0) userTeamRentService.insertBatch(insertUserTeamRents);
    }

    /**
     * 递归计算团队收益
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 用户团队等级DTO
     */
    private TeamLevelDTO teamReward(Long userId, Date date) {
        List<Long> childIds = userTreeService.getChildIds(userId);
        // 传递团队信息的集合
        List<TeamLevelDTO> dtos = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(childIds)) {
            for (Long childId : childIds) {
                dtos.add(teamReward(childId, date));
            }
        }
        // 开始计算团队Level
        if (CollectionUtils.isEmpty(dtos)) {
            // 如果传递的集合为空，说明为最下层用户，团队等级置为0（普通用户）
            // 查询用户的团队信息
            UserTeamExt userTeamExt = userTeamExtService.getUserTeamExt(userId);
            userTeamExt.setTeamLevel(TeamLevelEnum.LEVEL_0.getValue());
            userTeamExt.setUpdateTime(date);
            userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
            userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
            // 封装递归传递信息
            TeamLevelDTO teamLevelDTO = new TeamLevelDTO();
            teamLevelDTO.setUserId(userId);
            UserTeamDTO userTeamDTO = Optional.ofNullable(userRentService.getTotalStaticProfitAndTotalRentAsset(userId)).orElse(new UserTeamDTO());  // 查询总矿机价格与总静态收益
            teamLevelDTO.setTotalStaticProfit(userTeamDTO.getTotalStaticProfit());
            teamLevelDTO.setTotalRentAsset(userTeamDTO.getTotalRentAsset());
            teamLevelDTO.getUserTeamExts().add(userTeamExt);
            teamLevelDTO.getUserTeamRents().add(createUserTeamRent(userId, BigDecimal.ZERO, date));  // 记录每日用户团队收益
            return teamLevelDTO;
        } else {
            // 创建递归信息
            TeamLevelDTO teamLevelDTO = new TeamLevelDTO();
            teamLevelDTO.setUserId(userId);
            // 如果传递的集合不为空，遍历集合，回收下层递归的信息
            int maxTeamLevel = dtos.stream().mapToInt(TeamLevelDTO::getMaxTeamLevel).max().orElse(TeamLevelEnum.LEVEL_0.getValue());  // 最大团队等级
            BigDecimal totalStaticProfit = BigDecimal.ZERO;  // 累计静态收益
            BigDecimal totalRentAsset = BigDecimal.ZERO;  // 累计团队业绩
            for (TeamLevelDTO dto : dtos) {
                totalStaticProfit = totalStaticProfit.add(dto.getTotalStaticProfit());  // 累加静态收益
                totalRentAsset = totalRentAsset.add(dto.getTotalRentAsset());  // 累加团队业绩
                teamLevelDTO.getUserTeamExts().addAll(dto.getUserTeamExts());  // 用户团队信息更新集合合并
                teamLevelDTO.getUserAssets().addAll(dto.getUserAssets());  // 用户资产更新集合合并
                teamLevelDTO.getUserProfits().addAll(dto.getUserProfits());  // 用户收益更新集合合并
                teamLevelDTO.getUserAssetDetails().addAll(dto.getUserAssetDetails());  // 用户资产详情更新集合合并
                teamLevelDTO.getUserTeamRents().addAll(dto.getUserTeamRents());  // 用户每日团队收益添加集合合并
            }
            // 查询用户的团队信息，没有则创建一个
            UserTeamExt userTeamExt = userTeamExtService.getUserTeamExt(userId);
            userTeamExt.setTeamTotalStaticProfit(totalStaticProfit);
            userTeamExt.setTeamTotalRent(totalRentAsset);
            userTeamExt.setUpdateTime(date);
            // 判断用户团队等级
            // 如果manual_team_level字段(手动调整字段)为1，直接获取用户团队对象中的团队等级
            // 如果manual_team_level字段不为1，计算用户团队等级
            if (!userTeamExt.getManualTeamLevel().equals(TeamLevelManualEnum.MANUAL.getValue())) {
                // 计算团队等级以及用户俱乐部收益
                if (dtos.size() < 3 || totalRentAsset.compareTo(CLUB_PROFIT_STANDARD) < 0) {
                    // 若一级好友小于3个人或者团队业绩（矿机价格总和）小于50万，此用户俱乐部收益为0，等级为普通用户
                    userTeamExt.setTeamLevel(TeamLevelEnum.LEVEL_0.getValue());
                } else {
                    // 获取到团队最大等级前三名中最小的一个团队等级，此等级 + 1为该用户的等级，如果超过最大等级，则不升级
                    List<Integer> collect = dtos.stream().map(TeamLevelDTO::getMaxTeamLevel).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    ArrayList<Integer> integers = Lists.newArrayList(collect.get(0), collect.get(1), collect.get(2));
                    int minLevel = integers.stream().mapToInt(Integer::intValue).min().orElse(TeamLevelEnum.LEVEL_0.getValue());
                    int teamLevel = minLevel + 1 > TeamLevelEnum.LEVEL_4.getValue() ? minLevel : minLevel + 1;
                    userTeamExt.setTeamLevel(teamLevel);
                }
            }
            // 计算俱乐部收益
            // 如果用户等级不为0，说明为俱乐部用户，计算俱乐部收益，否则跳过
            int teamLevel = userTeamExt.getTeamLevel();
            if (teamLevel != TeamLevelEnum.LEVEL_0.getValue()) {
                // 如果存在静态收益，则计算俱乐部收益，否则不计算俱乐部收益，并将团队等级置为普通用户
                UserProfitDTO profitDTO = userProfitService.getCurrentDayUserProfit(userId, DATE_FORMAT.format(date));
                if (profitDTO == null) {
                    // 无静态收益，不发放俱乐部收益，将团队等级改为普通用户，如果是强制调整团队等级，不改变团队等级
                    if (!userTeamExt.getManualTeamLevel().equals(TeamLevelManualEnum.MANUAL.getValue()))
                        userTeamExt.setTeamLevel(TeamLevelEnum.LEVEL_0.getValue());
                } else {
                    // 判断是否已经超过当天俱乐部收益，只有总收益小于矿机总价格才发放俱乐部收益，超过不发放俱乐部收益
                    BigDecimal rentAsset = profitDTO.getTotalRentAsset();
                    if (profitDTO.getTotalProfit().compareTo(rentAsset) < 0) {
                        // 如果是皇家俱乐部，先不计算收益
                        // 如果不是皇家俱乐部，正常计算
                        if (teamLevel != TeamLevelEnum.LEVEL_4.getValue()) {
                            // 计算俱乐部收益
                            BigDecimal clubProfit = BigDecimal.ZERO;
                            for (TeamLevelDTO dto : dtos) {
                                if (dto.getTeamLevel() >= teamLevel) {
                                    // 下级大于等于自身的等级，俱乐部收益 = 下级的总静态收益 * 1%
                                    BigDecimal profit = dto.getTotalStaticProfit().multiply(new BigDecimal("0.01")).setScale(5, BigDecimal.ROUND_DOWN);
                                    clubProfit = clubProfit.add(profit);
                                } else {
                                    // 下级小于自身等级，俱乐部收益 = 下级的总静态收益 * 自身等级对应百分比 - 下级总静态收益排除下级自身的静态收益 * 下级等级对应百分比
                                    BigDecimal profit;
                                    // 下级总静态收益 * 自身等级对应百分比
                                    switch (teamLevel) {
                                        case 1:
                                            profit = dto.getTotalStaticProfit().multiply(TEAM_LEVEL1_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        case 2:
                                            profit = dto.getTotalStaticProfit().multiply(TEAM_LEVEL2_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        case 3:
                                            profit = dto.getTotalStaticProfit().multiply(TEAM_LEVEL3_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        default:
                                            profit = BigDecimal.ZERO;
                                    }
                                    // 下级总静态收益排除自身的静态收益
                                    BigDecimal dtoStatic;
                                    UserProfitDTO p = userProfitService.getCurrentDayUserProfit(dto.getUserId(), DATE_FORMAT.format(date));
                                    if (p == null)
                                        dtoStatic = BigDecimal.ZERO;
                                    else
                                        dtoStatic = p.getStaticProfit();
                                    BigDecimal d_ = dto.getTotalStaticProfit().subtract(dtoStatic).setScale(5, BigDecimal.ROUND_DOWN);
                                    BigDecimal profit_;
                                    // 排除后 * 下级等级对应百分比
                                    switch (dto.getTeamLevel()) {
                                        case 1:
                                            profit_ = d_.multiply(TEAM_LEVEL1_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        case 2:
                                            profit_ = d_.multiply(TEAM_LEVEL2_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        case 3:
                                            profit_ = d_.multiply(TEAM_LEVEL3_PROPORTION).setScale(5, BigDecimal.ROUND_DOWN);
                                            break;
                                        default:
                                            profit_ = BigDecimal.ZERO;
                                    }
                                    // 二者相减
                                    BigDecimal result = profit.subtract(profit_).setScale(5, BigDecimal.ROUND_DOWN);
                                    // 收益累加
                                    clubProfit = clubProfit.add(result);
                                }
                            }
                            // 添加俱乐部收益
                            BigDecimal staticProfit = profitDTO.getStaticProfit();
                            BigDecimal dynamicProfit = profitDTO.getDynamicProfit();
                            BigDecimal total = staticProfit.add(dynamicProfit).add(clubProfit);
                            if (total.compareTo(rentAsset) > 0) {
                                clubProfit = rentAsset.subtract(staticProfit).subtract(dynamicProfit).setScale(5, BigDecimal.ROUND_DOWN);
                            }
                            teamLevelDTO.getUserAssets().add(createUserAsset(userId, clubProfit, date));
                            teamLevelDTO.getUserProfits().add(createUserProfit(userId, staticProfit, dynamicProfit, clubProfit, rentAsset, date));
                            teamLevelDTO.getUserAssetDetails().add(createUserAssetDetail(userId, clubProfit,
                                    null, null, null, TransTypeEnum.Reward.getValue(), null, date));
                        }
                    }
                }
            }
            // 封装递归传递信息
            teamLevelDTO.setTeamLevel(userTeamExt.getTeamLevel());
            teamLevelDTO.setMaxTeamLevel(userTeamExt.getTeamLevel() > maxTeamLevel ? userTeamExt.getTeamLevel() : maxTeamLevel);
            UserTeamDTO userTeamDTO = Optional.ofNullable(userRentService.getTotalStaticProfitAndTotalRentAsset(userId)).orElse(new UserTeamDTO());  // 查询总矿机价格与总静态收益
            teamLevelDTO.setTotalStaticProfit(totalStaticProfit.add(userTeamDTO.getTotalStaticProfit()));  // 累加总静态收益
            teamLevelDTO.setTotalRentAsset(totalRentAsset.add(userTeamDTO.getTotalRentAsset()));  // 累加总矿机价格
            teamLevelDTO.getUserTeamExts().add(userTeamExt);
            teamLevelDTO.getUserTeamRents().add(createUserTeamRent(userId, totalRentAsset, date));  // 记录每日用户团队收益
            return teamLevelDTO;
        }
    }

    /**
     * 创建用户资产DTO
     * 用于更新用户资产
     *
     * @param userId      用户ID
     * @param totalProfit 总收益
     * @param date        日期
     * @return 用户资产DTO
     */
    private UserAssetDTO createUserAsset(Long userId, BigDecimal totalProfit, Date date) {
        UserAssetDTO userAsset = new UserAssetDTO();
        userAsset.setUserId(userId);  // 更新条件为用户ID
        userAsset.setProfit(totalProfit);
        userAsset.setUpdateTime(date);
        return userAsset;
    }

    /**
     * 创建用户收益
     *
     * @param userId        用户ID
     * @param staticProfit  静态收益
     * @param dynamicProfit 动态收益
     * @param clubProfit    俱乐部收益
     * @param totalProfit   总收益
     * @param date          日期
     * @return 用户收益对象
     */
    private UserProfit createUserProfit(Long userId, BigDecimal staticProfit, BigDecimal dynamicProfit, BigDecimal clubProfit, BigDecimal totalProfit, Date date) {
        UserProfit userProfit = new UserProfit();
        userProfit.setUserId(userId);
        userProfit.setStaticProfit(staticProfit);
        userProfit.setDynamicProfit(dynamicProfit);
        userProfit.setClubProfit(clubProfit);
        userProfit.setTotalProfit(totalProfit);
        if (date != null) {
            userProfit.setDate(date);
            userProfit.setCreateTime(date);
        }
        return userProfit;
    }

    /**
     * 创建用户资产详情
     *
     * @param userId       用户ID
     * @param money        金额
     * @param rentId       矿机ID
     * @param profit       带本金收益
     * @param profitExt    不带本金的收益
     * @param transType    交易类型
     * @param rewardUserId 从哪个用户获取的推荐人收益
     * @param date         日期
     * @return 用户资产详情对象
     */
    private UserAssetDetail createUserAssetDetail(Long userId, BigDecimal money, Long rentId, BigDecimal profit,
                                                  BigDecimal profitExt, Integer transType, Long rewardUserId, Date date) {
        UserAssetDetail detail = new UserAssetDetail();
        detail.setUserId(userId);
        detail.setMoney(money);
        detail.setRentId(rentId);
        detail.setProfit(profit);  // 带本金的收益
        detail.setProfitExt(profitExt);   // 不带本金的收益
        detail.setTransType(transType);
        detail.setDigDate(date);
        detail.setRewardUserId(rewardUserId);
        detail.setCreateTime(date);
        detail.setUpdateTime(date);
        detail.setVersionType(0);  // 冗余字段
        return detail;
    }

    /**
     * 创建每日记录团队业绩对象
     *
     * @param userId    用户ID
     * @param totalRent 团队业绩
     * @param date      日期
     * @return 团队业绩记录对象
     */
    private UserTeamRent createUserTeamRent(Long userId, BigDecimal totalRent, Date date) {
        UserTeamRent teamRent = new UserTeamRent();
        teamRent.setUserId(userId);
        teamRent.setTeamTotalRent(totalRent);
        teamRent.setCreateDate(date);
        teamRent.setCreateTime(date);
        return teamRent;
    }

}
