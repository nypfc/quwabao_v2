package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.dataobj.model.UserAsset;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserAssetService;
import com.gedoumi.quwabao.user.service.UserRentService;
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

/**
 * 定时任务执行
 *
 * @author Minced
 */
@Slf4j
@Service
public class RunScheduleTask {

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private SysRentService sysRentService;

    @Resource
    private UserRentService userRentService;

    /**
     * 计算挖矿收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void digTask() {
        // 查询所有正在使用中的矿机
        List<UserRent> userRents = userRentService.getAllUserActiveRent();
        if (CollectionUtils.isEmpty(userRents)) {
            log.info("没有符合条件的矿机");
            return;
        }
        log.info("userRents size = {} ", userRents.size());

        Date now = new Date();
        // 批量更新的集合
        ArrayList<UserRent> updateUserRents = Lists.newArrayList();
        ArrayList<UserAssetDetail> updateUserAssetDetails = Lists.newArrayList();
        HashMap<Long, BigDecimal> map = Maps.newHashMap();
        ArrayList<UserAsset> updateUserAssets = Lists.newArrayList();
        // 获取所有已租用的矿机种类集合
        Set<Integer> rentTypes = userRents.stream().map(UserRent::getRentType).collect(Collectors.toSet());
        // 获取矿机集合
        List<SysRent> sysRents = sysRentService.getRentsInType(rentTypes);

        // 遍历用户矿机集合
        userRents.forEach(userRent -> sysRents.forEach(sysRent -> {
            // 如果矿机类型不匹配跳过此次循环
            if (!userRent.getRentType().equals(sysRent.getRentCode()))
                return;
            BigDecimal rentAsset = userRent.getRentAsset();  // 矿机价格
            BigDecimal profitDay = sysRent.getProfitDay();  // 当前每日产币量
            BigDecimal alreadyDig = userRent.getAlreadyDig();  // 已经获得的币量
            BigDecimal totalAsset = userRent.getTotalAsset();  // 总币量
            BigDecimal remainAsset = totalAsset.subtract(alreadyDig);  // 剩余币量
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
                Long userId = userRent.getUserId();
                map.put(userId, map.getOrDefault(userId, BigDecimal.ZERO).add(profitDay));
                // 向添加用户资产详情集合中增加数据
                BigDecimal totalDay = totalAsset.divide(profitDay, 5, BigDecimal.ROUND_DOWN);  // 计算总共需要多少天挖完
                BigDecimal profit = totalAsset.subtract(rentAsset).divide(totalDay, 5, BigDecimal.ROUND_DOWN);  // 计算带本金的收益
                UserAssetDetail detail = createUserAssetDetail(userRent.getUserId(), profitDay, userRent.getId(),
                        profit, profitDay, TransTypeEnum.Profit.getValue());
                updateUserAssetDetails.add(detail);
            }
        }));
        // 遍历用户总收益
        map.forEach((userId, totalProfit) -> {
            UserAsset userAsset = new UserAsset();
            userAsset.setUserId(userId);
            userAsset.setProfit(userAsset.getProfit().add(totalProfit));
            userAsset.setRemainAsset(userAsset.getRemainAsset().add(totalProfit));
            userAsset.setTotalAsset(userAsset.getRemainAsset().add(userAsset.getFrozenAsset()));  // 余额 + frozenAsset = totalAsset
            updateUserAssets.add(userAsset);
//            UserProfit userProfit = userProfitDao.findByDate(DATE_FORMAT.format(now), userId).orElse(new UserProfit());
//            userProfit.setUserId(userId);
//            userProfit.setStaticProfit(totalProfit);
//            userProfit.setTotalProfit(totalProfit);
//            userProfit.setCreateTime(now);
//            userProfit.setDate(now);
//            userProfitDao.save(userProfit);
        });
        // 批量更新矿机
        userRentService.updateBatch(updateUserRents);
        // 批量更新用户资产
        userAssetService.updateBatch(updateUserAssets);
        // 批量添加用户资产详情
        userAssetDetailService.insertBatch(updateUserAssetDetails);
    }

    /**
     * 计算推荐人收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask() {

    }

    /**
     * 计算俱乐部收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void clubRewardTask() {

    }

    /**
     * 创建用户资产详情
     *
     * @param userId    用户ID
     * @param money     金额
     * @param rentId    矿机ID
     * @param profit    带本金收益
     * @param profitExt 不带本金的收益
     * @param transType 交易类型
     * @return 用户资产详情对象
     */
    private UserAssetDetail createUserAssetDetail(Long userId, BigDecimal money, Long rentId, BigDecimal profit, BigDecimal profitExt, Integer transType) {
        UserAssetDetail detail = new UserAssetDetail();
        Date now = new Date();
        detail.setUserId(userId);
        detail.setMoney(money);
        detail.setRentId(rentId);
        detail.setProfit(profit);  // 带本金的收益
        detail.setProfitExt(profitExt);   // 不带本金的收益
        detail.setTransType(transType);
        detail.setDigDate(now);
        detail.setCreateTime(now);
        detail.setUpdateTime(now);
        detail.setVersionType(0);  // 冗余字段
        return detail;
    }

}
