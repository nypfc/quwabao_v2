package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserAssetService;
import com.gedoumi.quwabao.user.service.UserRentService;
import com.gedoumi.quwabao.user.service.UserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定时任务执行
 *
 * @author Minced
 */
@Slf4j
@Service
public class RunScheduleTask {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private UserService userService;

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
        // 当日日期
        Date now = new Date();
        //
        HashMap<Long, BigDecimal> map = Maps.newHashMap();
        // 查询所有正在使用中的矿机
        List<UserRent> userRents = userRentService.getAllUserActiveRent();
        if (CollectionUtils.isEmpty(userRents)) {
            log.info("没有符合条件的矿机");
            return;
        }
        log.info("userRents size = {} ", userRents.size());
        // 获取所有已租用的矿机种类集合
        Set<Integer> rentTypes = userRents.stream().map(UserRent::getRentType).collect(Collectors.toSet());
        // 获取矿机集合
        List<SysRent> sysRents = sysRentService.getRentsInType(rentTypes);

//        userRents.forEach(userRent -> sysRents.forEach(sysRent -> {
//            // 如果矿机类型不匹配跳过此次循环
//            if (!StringUtils.equals(String.valueOf(userRent.getRentType()), sysRent.getCode()))
//                return;
//            BigDecimal rentAsset = userRent.getRentAsset();  // 矿机价格
//            BigDecimal profitDay = sysRent.getProfitDay();  // 当前每日产币量
//            BigDecimal alreadyDig = userRent.getAlreadyDig();  // 已经获得的币量
//            BigDecimal totalAsset = userRent.getTotalAsset();  // 总币量
//            BigDecimal remainAsset = totalAsset.subtract(alreadyDig);  // 剩余币量
//            // 如果剩余币量不足每日的产币量，则直接结束矿机
//            if (remainAsset.compareTo(profitDay) < 0) {
//                userRent.setExpireDate(now);
//                userRent.setRentStatus(RentStatus.Expired.getValue());
//                userRent.setUpdateTime(now);
//                userRentDao.save(userRent);
//            } else {
//                userRent.setLastDig(profitDay);
//                userRent.setAlreadyDig(alreadyDig.add(profitDay));
//                userRent.setUpdateTime(now);
//                // 如果已挖矿量与总收益相同，说明正好挖完，将矿机置为停用
//                if (userRent.getAlreadyDig().compareTo(totalAsset) == 0) {
//                    userRent.setRentStatus(RentStatus.Expired.getValue());
//                }
//                userRentDao.save(userRent);
//                // 累加用户矿机总收益
//                User user = userRent.getUser();
//                map.put(user.getId(), map.getOrDefault(user.getId(), BigDecimal.ZERO).add(profitDay));  // 余额 + frozenAsset = totalAsset
//                // 向添加用户资产详情集合中增加数据
//                UserAssetDetail userAssetDetail = new UserAssetDetail();
//                userAssetDetail.setUser(user);
//                userAssetDetail.setUserRent(userRent);
//                userAssetDetail.setMoney(profitDay);
//                BigDecimal totalDay = totalAsset.divide(profitDay, 5, BigDecimal.ROUND_DOWN);  // 计算总共需要多少天挖完
//                BigDecimal profit = totalAsset.subtract(rentAsset).divide(totalDay, 5, BigDecimal.ROUND_DOWN);  // 计算带本金的收益
//                userAssetDetail.setProfit(profit);  // 带本金的收益
//                userAssetDetail.setProfitExt(profitDay);   // 不带本金的收益
//                userAssetDetail.setTransType(TransType.Profit.getValue());
//                userAssetDetail.setDigDate(now);
//                userAssetDetail.setCreateTime(now);
//                userAssetDetail.setUpdateTime(now);
//                userAssetDetail.setVersionType(appConfig.getVersion().getValue());
//                assetDetailDao.save(userAssetDetail);
//            }
//        }));
//        // 添加用户每日收益数据
//        map.forEach((userId, totalProfit) -> {
//            User user = userDao.findOne(userId);
//            UserAsset userAsset = assetDao.findByUser(user);
//            userAsset.setProfit(userAsset.getProfit().add(totalProfit));
//            userAsset.setRemainAsset(userAsset.getRemainAsset().add(totalProfit));
//            userAsset.setTotalAsset(userAsset.getRemainAsset().add(userAsset.getFrozenAsset()));  // 余额 + frozenAsset = totalAsset
//            assetDao.save(userAsset);
//            UserProfit userProfit = userProfitDao.findByDate(sdf.format(now), user.getId()).orElse(new UserProfit());
//            userProfit.setUser(user);
//            userProfit.setStaticProfit(totalProfit);
//            userProfit.setTotalProfit(totalProfit);
//            userProfit.setCreateTime(now);
//            userProfit.setDate(now);
//            userProfitDao.save(userProfit);
//        });
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

}
