package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.schedule.dataobj.dto.TeamLevelDTO;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.service.SysConfigService;
import com.gedoumi.quwabao.user.dataobj.dto.UserAssetDTO;
import com.gedoumi.quwabao.user.dataobj.dto.UserProfitDTO;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.dataobj.model.UserTree;
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

import static com.gedoumi.quwabao.common.constants.Constants.DATE_FORMAT;

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
        log.info("矿机数量:{} ", rentDTOS.size());
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
                UserAssetDetail detail = createUserAssetDetail(rentDTO.getUserId(), profitDay, rentDTO.getId(),
                        profit, profitDay, TransTypeEnum.Profit.getValue(), null, now);
                insertUserAssetDetails.add(detail);
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
        userRentService.updateBatch(updateUserRents);
        // 批量更新用户资产
        userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户收益
        userProfitService.insertBatch(insertUserProfits);
        // 批量添加用户资产详情
        userAssetDetailService.insertBatch(insertUserAssetDetails);
    }

    /**
     * 计算推荐人收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask() {
        // 当前日期
        Date now = new Date();
        // 批量更新的集合
        ArrayList<UserAssetDetail> updateUserAssetDetails = Lists.newArrayList();
        HashMap<Long, BigDecimal> map = Maps.newHashMap();
        ArrayList<UserAssetDTO> updateUserAssets = Lists.newArrayList();
        ArrayList<UserProfit> updateUserProfits = Lists.newArrayList();
        // 获取系统配置
        SysConfig sysConfig = sysConfigService.getSysConfig();
        // 遍历用户
        List<UserProfit> userProfits = userProfitService.getCurrentDayUserProfits(DATE_FORMAT.format(now));
        userProfits.forEach(up -> {
            StringBuilder sb = new StringBuilder();
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
                    sb.append(parentId).append(":").append(dynamicReward).append(" -> ");
                    // 累加用户的动态收益
                    map.put(parentId, map.getOrDefault(parentId, BigDecimal.ZERO).add(dynamicReward));
                    // 资产详情按正常收益储存，用于数据记录
                    // 如果为收益0，不添加数据
                    if (dynamicReward.compareTo(BigDecimal.ZERO) != 0) {
                        UserAssetDetail userAssetDetail = createUserAssetDetail(parentId, dynamicReward, null, BigDecimal.ZERO, BigDecimal.ZERO,
                                TransTypeEnum.Reward.getValue(), userId, now);
                        updateUserAssetDetails.add(userAssetDetail);
                    }
                } else {
                    sb.append(list.get(i)).append(":").append("0.00000").append(" -> ");
                }
            }
            log.info(sb.toString());
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
        userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户收益
        userProfitService.updateBatch(updateUserProfits);
        // 批量添加用户资产详情
        userAssetDetailService.insertBatch(updateUserAssetDetails);
    }

    /**
     * 计算俱乐部收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void clubRewardTask() {
        // 当前日期
        Date now = new Date();
        // 遍历当日收益，递归用户树
        userService.getTeamLeaderIds().forEach(userId -> teamReward(userId, now));
//        // 获取所有皇家俱乐部的用户
//        List<Long> userIds = userTeamExtDao.findByTeamLevel(TeamLevel.LEVEL_4.getValue()).stream().map(userTeamExt -> userTeamExt.getUser().getId()).collect(Collectors.toList());
//        int count = userIds.size();
//        if (count > 0) {
//            // 平分当天所有产币量的1%
//            BigDecimal totalStaticProfit = userProfitDao.findTotalStaticProfit(this.sdf.format(now)).orElse(BigDecimal.ZERO);
//            BigDecimal p_ = totalStaticProfit.multiply(new BigDecimal("0.01")).setScale(5, BigDecimal.ROUND_DOWN);
//            BigDecimal clubProfit = p_.divide(new BigDecimal(count), 5, BigDecimal.ROUND_DOWN);
//            for (UserProfit userProfit : userProfitDao.findByDate(this.sdf.format(now), userIds)) {
//                User user = userProfit.getUser();
//                // 如果当天总收益超过了正在激活矿机的总价格，总收益置为总矿机价格，俱乐部多余部分减去，剩余的部分为实际的俱乐部收益
//                BigDecimal rentAsset = userRentDao.findTotalRentAsset(user.getId()).orElse(BigDecimal.ZERO);
//                // 添加俱乐部收益
//                userProfit.setClubProfit(clubProfit);
//                addClub(clubProfit, rentAsset, userProfit, now);
//                List<String> childs = userTreeDao.findByParent(user).stream().map(userTree -> userTree.getChild().getMobilePhone()).collect(Collectors.toList());
//                log.info("userMobile:{}; clubProfit:{}; teamLevel:4; childUser:{}", user.getMobilePhone(), clubProfit, childs);
//            }
//        }
    }

    /**
     * 递归计算团队收益
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 用户团队等级DTO
     */
    private TeamLevelDTO teamReward(Long userId, Date date) {
//        List<User> childUsers = userTreeDao.findByParent(user).stream().map(UserTree::getChild).collect(Collectors.toList());
//        // 传递团队信息的集合
//        List<TeamLevelDTO> dtos = Lists.newArrayList();
//        if (!CollectionUtils.isEmpty(childUsers)) {
//            for (User childUser : childUsers) {
//                dtos.add(teamReward(childUser, date));
//            }
//        }
//        // 开始计算团队Level
//        if (CollectionUtils.isEmpty(dtos)) {
//            // 如果传递的集合为空，说明为最下层用户，团队等级置为0（普通用户）
//
//            // 查询用户的团队信息，没有则创建一个
//            UserTeamExt userTeamExt = userTeamExtDao.findByUserId(user.getId()).orElseGet(UserTeamExt::new);
//            userTeamExt.setUser(user);
//            userTeamExt.setTeamLevel(TeamLevel.LEVEL_0.getValue());
//            userTeamExt.setUpdateTime(date);
//            userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
//            userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
//            userTeamExtDao.save(userTeamExt);
//            // 封装递归传递信息
//            TeamLevelDTO teamLevelDTO = new TeamLevelDTO();
//            teamLevelDTO.setUserId(user.getId());
//            teamLevelDTO.setMobile(user.getMobilePhone());
//            teamLevelDTO.setTeamLevel(TeamLevel.LEVEL_0.getValue());
//            // 查询静态收益
//            teamLevelDTO.setTotalStaticProfit(userProfitDao.findStaticProfit(this.sdf.format(date), user.getId()).orElse(BigDecimal.ZERO));
//            // 查询用户总矿机价格
//            teamLevelDTO.setTotalRentAsset(userRentDao.findTotalRentAsset(user.getId()).orElse(BigDecimal.ZERO));
//            // 记录每日用户团队收益
//            UserTeamRent teamRent = new UserTeamRent();
//            teamRent.setUser(user);
//            userTeamRentService.insert(teamRent);
//            // 日志记录
//            log.info("userMobile:{}; bottom user", user.getMobilePhone());
//            return teamLevelDTO;
//        } else {
//            // 如果传递的集合不为空，遍历集合，回收下层递归的信息
//            int maxTeamLevel = dtos.stream().mapToInt(TeamLevelDTO::getMaxTeamLevel).max().orElse(TeamLevel.LEVEL_0.getValue());  // 最大团队等级
//            BigDecimal totalStaticProfit = BigDecimal.ZERO;  // 累计静态收益
//            BigDecimal totalRentAsset = BigDecimal.ZERO;  // 累计团队业绩
//            for (TeamLevelDTO dto : dtos) {
//                // 累计静态收益
//                totalStaticProfit = totalStaticProfit.add(dto.getTotalStaticProfit());
//                // 累加团队业绩
//                totalRentAsset = totalRentAsset.add(dto.getTotalRentAsset());
//            }
//            // 记录每日用户团队收益
//            UserTeamRent teamRent = new UserTeamRent();
//            teamRent.setUser(user);
//            userTeamRentService.insert(teamRent);
//            // 查询用户的团队信息，没有则创建一个
//            UserTeamExt userTeamExt = userTeamExtDao.findByUserId(user.getId()).orElseGet(UserTeamExt::new);
//            userTeamExt.setUser(user);
//            userTeamExt.setTeamTotalStaticProfit(totalStaticProfit);
//            userTeamExt.setTeamTotalRent(totalRentAsset);
//            userTeamExt.setUpdateTime(date);
//            // 计算团队等级以及用户俱乐部收益
//            if (dtos.size() < 3 || totalRentAsset.compareTo(new BigDecimal("500000.00000")) < 0) {
//                // 若一级好友小于3个人或者团队业绩（矿机价格总和）小于50万，此用户俱乐部收益为0，等级为普通用户
//                userTeamExt.setTeamLevel(TeamLevel.LEVEL_0.getValue());
//                log.info("userMobile:{}; clubProfit:child<3 or total<500000; teamLevel:0; childUser:{}", user.getMobilePhone(), dtos.stream().map(TeamLevelDTO::getMobile).collect(Collectors.toList()));
//            } else {
//                // 否则用户为俱乐部用户
//                // 获取到团队最大等级前三名中最小的一个团队等级，此等级 + 1为该用户的等级，如果超过最大等级，则不升级
//                List<Integer> collect = dtos.stream().map(TeamLevelDTO::getMaxTeamLevel).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//                ArrayList<Integer> integers = Lists.newArrayList(collect.get(0), collect.get(1), collect.get(2));
//                int minLevel = integers.stream().mapToInt(Integer::intValue).min().orElse(TeamLevel.LEVEL_0.getValue());
//                int teamLevel = minLevel + 1 > TeamLevel.LEVEL_4.getValue() ? minLevel : minLevel + 1;
//                userTeamExt.setTeamLevel(teamLevel);
//                // 如果存在静态收益，则计算俱乐部收益，否则不计算俱乐部收益，并将团队等级置为普通用户
//                Optional<UserProfit> userProfit_ = userProfitDao.findByDate(this.sdf.format(date), user.getId());
//                if (!userProfit_.isPresent()) {
//                    // 无静态收益，不发放俱乐部收益，团队等级为普通用户
//                    userTeamExt.setTeamLevel(TeamLevel.LEVEL_0.getValue());
//                    log.info("userMobile:{}; no staticProfit; teamLevel:0; childUser:{}", user.getMobilePhone(), dtos.stream().map(TeamLevelDTO::getMobile).collect(Collectors.toList()));
//                } else {
//                    UserProfit userProfit = userProfit_.get();
//                    // 判断是否已经超过当天俱乐部收益，超过则不发放俱乐部收益
//                    BigDecimal rentAsset = userRentDao.totalUserRentMoney(user.getId(), RentStatus.Active.getValue()).orElse(BigDecimal.ZERO);
//                    if (userProfit.getTotalProfit().compareTo(rentAsset) >= 0) {
//                        // 超过当天总矿机价格
//                        log.info("userMobile:{}; clubProfit:over limit; teamLevel:{}; childUser:{}", user.getMobilePhone(), teamLevel, dtos.stream().map(TeamLevelDTO::getMobile).collect(Collectors.toList()));
//                    } else {
//                        // 如果不是皇家俱乐部，正常计算，否则先不计算收益
//                        if (teamLevel != TeamLevel.LEVEL_4.getValue()) {
//                            // 计算俱乐部收益
//                            BigDecimal clubProfit = BigDecimal.ZERO;
//                            for (TeamLevelDTO dto : dtos) {
//                                if (dto.getTeamLevel() >= teamLevel) {
//                                    // 下级大于等于自身的等级，俱乐部收益 = 下级的总静态收益 * 1%
//                                    BigDecimal profit = dto.getTotalStaticProfit().multiply(new BigDecimal("0.01")).setScale(5, BigDecimal.ROUND_DOWN);
//                                    clubProfit = clubProfit.add(profit);
//                                } else {
//                                    // 下级小于自身等级，俱乐部收益 = 下级的总静态收益 * 自身等级对应百分比 - 下级总静态收益排除自身的静态收益 * 下级等级对应百分比
//                                    BigDecimal profit;
//                                    // 下级总静态收益 * 自身等级对应百分比
//                                    switch (teamLevel) {
//                                        case 1:
//                                            profit = dto.getTotalStaticProfit().multiply(new BigDecimal("0.03")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        case 2:
//                                            profit = dto.getTotalStaticProfit().multiply(new BigDecimal("0.06")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        case 3:
//                                            profit = dto.getTotalStaticProfit().multiply(new BigDecimal("0.09")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        default:
//                                            profit = BigDecimal.ZERO;
//                                    }
//                                    // 下级总静态收益排除自身的静态收益
//                                    BigDecimal dtoStatic = userProfitDao.findStaticProfit(this.sdf.format(date), dto.getUserId()).orElse(BigDecimal.ZERO);
//                                    BigDecimal d_ = dto.getTotalStaticProfit().subtract(dtoStatic).setScale(5, BigDecimal.ROUND_DOWN);
//                                    BigDecimal profit_;
//                                    // 排除后 * 下级等级对应百分比
//                                    switch (dto.getTeamLevel()) {
//                                        case 1:
//                                            profit_ = d_.multiply(new BigDecimal("0.03")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        case 2:
//                                            profit_ = d_.multiply(new BigDecimal("0.06")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        case 3:
//                                            profit_ = d_.multiply(new BigDecimal("0.09")).setScale(5, BigDecimal.ROUND_DOWN);
//                                            break;
//                                        default:
//                                            profit_ = BigDecimal.ZERO;
//                                    }
//                                    // 二者相减
//                                    BigDecimal result = profit.subtract(profit_).setScale(5, BigDecimal.ROUND_DOWN);
//                                    // 收益累加
//                                    clubProfit = clubProfit.add(result);
//                                }
//                            }
//                            // 更新用户收益
//                            userProfit.setClubProfit(clubProfit);
//                            // 添加俱乐部收益
//                            addClub(clubProfit, rentAsset, userProfit, date);
//                            log.info("userMobile:{}; clubProfit:{}; teamLevel:{}; childUser:{}", user.getMobilePhone(), clubProfit, teamLevel, dtos.stream().map(TeamLevelDTO::getMobile).collect(Collectors.toList()));
//                        }
//                    }
//                }
//            }
//            userTeamExtDao.save(userTeamExt);
//
//            // 封装递归传递信息
//            TeamLevelDTO teamLevelDTO = new TeamLevelDTO();
//            teamLevelDTO.setUserId(user.getId());
//            teamLevelDTO.setMobile(user.getMobilePhone());
//            teamLevelDTO.setTeamLevel(userTeamExt.getTeamLevel());
//            teamLevelDTO.setMaxTeamLevel(userTeamExt.getTeamLevel() > maxTeamLevel ? userTeamExt.getTeamLevel() : maxTeamLevel);
//            // 累加总静态收益
//            BigDecimal static_ = userProfitDao.findStaticProfit(this.sdf.format(date), user.getId()).orElse(BigDecimal.ZERO);
//            teamLevelDTO.setTotalStaticProfit(totalStaticProfit.add(static_));
//            // 累加总矿机价格
//            BigDecimal rentAsset_ = userRentDao.findTotalRentAsset(user.getId()).orElse(BigDecimal.ZERO);
//            teamLevelDTO.setTotalRentAsset(totalRentAsset.add(rentAsset_));
//            return teamLevelDTO;
//        }
        return null;
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

}
