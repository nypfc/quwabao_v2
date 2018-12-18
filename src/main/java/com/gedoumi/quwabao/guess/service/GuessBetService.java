package com.gedoumi.quwabao.guess.service;

import com.gedoumi.common.enums.GuessBetStatusEnum;
import com.gedoumi.common.enums.GuessModeEnum;
import com.gedoumi.common.enums.TransType;
import com.gedoumi.guess.dao.*;
import com.gedoumi.guess.entity.*;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.guess.dataobj.form.BetForm;
import com.gedoumi.quwabao.guess.dataobj.form.BetFormValidate;
import com.gedoumi.quwabao.guess.dataobj.model.GuessBet;
import com.gedoumi.quwabao.guess.dataobj.model.GuessDetail;
import com.gedoumi.quwabao.guess.mapper.GuessBetMapper;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户下注Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class GuessBetService {

    @Resource
    private GuessBetMapper guessBetMapper;

    /**
     * 根据用户ID查询下注集合
     *
     * @param userId 用户ID
     * @param page   当前页
     * @param rows   每页数据量
     * @return 下注集合
     */
    public List<GuessBet> getBets(Long userId, String page, String rows) {
        try {
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        } catch (NumberFormatException e) {
            throw new BusinessException(CodeEnum.ParamError);
        }
        PageInfo<GuessBet> pageInfo = new PageInfo<>(guessBetMapper.selectByUserId(userId));
        return pageInfo.getList();
    }

    /**
     * 用户下注
     *
     * @param betForm 下注表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void bet(BetForm betForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取参数
        String bet = betForm.getBet();
        String guessMode = betForm.getGuessMode();
        String guessNumber = betForm.getGuessNumber();

        // 参数验证
        String message = BetFormValidate.validate(bet, guessMode, guessNumber);
        if (message != null) {
            responseObject.setInfo(CodeEnum.ParamError);
            responseObject.setData(message);
            return responseObject;
        }
        // 获取竞猜详情，未获取到则非竞猜期，直接返回
        GuessDetail guessDetail = guessDetailService.getBettingGuessDetail();
        if (guessDetail == null) {
            responseObject.setInfo(CodeEnum.NotBetting);
            return responseObject;
        }
        // 更新用户资产，如果返回false说明余额不足

        // 创建下注记录
        GuessBet guessBet = new GuessBet();
        guessBet.setUserId(user.getId());
        guessBet.setGuessDetailId(guessDetail.getId());
        guessBet.setBetTime(new Date());
        guessBet.setGuessMode(guessMode);
        guessBet.setBetMoney(bet_);
        guessBet.setGuessNumber(guessNumber);
        guessBet.setBetOdds(guessBetService.updateGuessDetailMoney(guessBet));  // 更新对应选项的投注金额，并获得其赔率
        guessBetMapper.insert(guessBet);
        // 创建竞猜用户资产详情
        guessBetService.createUserAssetDetail(user.getId(), bet_, BigDecimal.ZERO, TransTypeEnum.Bet.getValue());
    }

    /**
     * 更新下注选项的金额并获取对应的赔率
     *
     * @param guessBet 下注对象
     * @return 对应的赔率
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal updateGuessDetailMoney(GuessBet guessBet) {
        // 获取参数
        Long guessDetailId = guessBet.getGuessDetailId();
        Integer guessMode = guessBet.getGuessMode();
        String guessNumber = guessBet.getGuessNumber();
        BigDecimal bet = guessBet.getBetMoney();
        // 选项对应赔率
        BigDecimal odds;
        // 根据不同玩法更新对应的选项的下注金额
        if (guessMode.equals(GuessModeEnum.MODE_1.getMode())) {
            // 获取竞猜详情对应金额和赔率
            GuessDetailMoney1 detailMoney = guessDetailMoney1Dao.findByGuessDetailId(guessDetailId);
            GuessDetailOdds1 detailOdds = guessDetailOdds1Dao.findByGuessDetailId(guessDetailId);
            // 增加选项的金额以及获取对应赔率
            int number = Integer.parseInt(guessNumber);
            if (number == 1) {
                odds = detailOdds.getO1();
                detailMoney.setM1(detailMoney.getM1().add(bet));
            } else if (number == 2) {
                odds = detailOdds.getO2();
                detailMoney.setM2(detailMoney.getM2().add(bet));
            } else if (number == 3) {
                odds = detailOdds.getO3();
                detailMoney.setM3(detailMoney.getM3().add(bet));
            } else if (number == 4) {
                odds = detailOdds.getO4();
                detailMoney.setM4(detailMoney.getM4().add(bet));
            } else if (number == 5) {
                odds = detailOdds.getO5();
                detailMoney.setM5(detailMoney.getM5().add(bet));
            } else if (number == 6) {
                odds = detailOdds.getO6();
                detailMoney.setM6(detailMoney.getM6().add(bet));
            } else {
                odds = BigDecimal.ZERO;
            }
            // 更新金额
            guessDetailMoney1Dao.save(detailMoney);
        } else if (guessMode.equals(GuessModeEnum.MODE_2.getMode())) {
            // 获取竞猜详情对应金额和赔率
            GuessDetailMoney2 detailMoney = guessDetailMoney2Dao.findByGuessDetailId(guessDetailId);
            GuessDetailOdds2 detailOdds = guessDetailOdds2Dao.findByGuessDetailId(guessDetailId);
            // 获取前两名
            int number1 = Integer.parseInt(String.valueOf(guessNumber.charAt(0)));
            int number2 = Integer.parseInt(String.valueOf(guessNumber.charAt(1)));
            // 增加选项的金额以及获取对应赔率
            if ((number1 == 1 && number2 == 2) || (number1 == 2 && number2 == 1)) {
                odds = detailOdds.getO12();
                detailMoney.setM12(detailMoney.getM12().add(bet));
            } else if ((number1 == 1 && number2 == 3) || (number1 == 3 && number2 == 1)) {
                odds = detailOdds.getO13();
                detailMoney.setM13(detailMoney.getM13().add(bet));
            } else if ((number1 == 1 && number2 == 4) || (number1 == 4 && number2 == 1)) {
                odds = detailOdds.getO14();
                detailMoney.setM14(detailMoney.getM14().add(bet));
            } else if ((number1 == 1 && number2 == 5) || (number1 == 5 && number2 == 1)) {
                odds = detailOdds.getO15();
                detailMoney.setM15(detailMoney.getM15().add(bet));
            } else if ((number1 == 1 && number2 == 6) || (number1 == 6 && number2 == 1)) {
                odds = detailOdds.getO16();
                detailMoney.setM16(detailMoney.getM16().add(bet));
            } else if ((number1 == 2 && number2 == 3) || (number1 == 3 && number2 == 2)) {
                odds = detailOdds.getO23();
                detailMoney.setM23(detailMoney.getM23().add(bet));
            } else if ((number1 == 2 && number2 == 4) || (number1 == 4 && number2 == 2)) {
                odds = detailOdds.getO24();
                detailMoney.setM24(detailMoney.getM24().add(bet));
            } else if ((number1 == 2 && number2 == 5) || (number1 == 5 && number2 == 2)) {
                odds = detailOdds.getO25();
                detailMoney.setM25(detailMoney.getM25().add(bet));
            } else if ((number1 == 2 && number2 == 6) || (number1 == 6 && number2 == 2)) {
                odds = detailOdds.getO26();
                detailMoney.setM26(detailMoney.getM26().add(bet));
            } else if ((number1 == 3 && number2 == 4) || (number1 == 4 && number2 == 3)) {
                odds = detailOdds.getO34();
                detailMoney.setM34(detailMoney.getM34().add(bet));
            } else if ((number1 == 3 && number2 == 5) || (number1 == 5 && number2 == 3)) {
                odds = detailOdds.getO35();
                detailMoney.setM35(detailMoney.getM35().add(bet));
            } else if ((number1 == 3 && number2 == 6) || (number1 == 6 && number2 == 3)) {
                odds = detailOdds.getO36();
                detailMoney.setM36(detailMoney.getM36().add(bet));
            } else if ((number1 == 4 && number2 == 5) || (number1 == 5 && number2 == 4)) {
                odds = detailOdds.getO45();
                detailMoney.setM45(detailMoney.getM45().add(bet));
            } else if ((number1 == 4 && number2 == 6) || (number1 == 6 && number2 == 4)) {
                odds = detailOdds.getO46();
                detailMoney.setM46(detailMoney.getM46().add(bet));
            } else if ((number1 == 5 && number2 == 6) || (number1 == 6 && number2 == 5)) {
                odds = detailOdds.getO56();
                detailMoney.setM56(detailMoney.getM56().add(bet));
            } else {
                odds = BigDecimal.ZERO;
            }
            // 更新金额
            guessDetailMoney2Dao.save(detailMoney);
        } else {
            // 获取竞猜详情对应金额和赔率
            GuessDetailMoney3 detailMoney = guessDetailMoney3Dao.findByGuessDetailId(guessDetailId);
            GuessDetailOdds3 detailOdds = guessDetailOdds3Dao.findByGuessDetailId(guessDetailId);
            // 增加选项的金额以及获取对应赔率
            if (Integer.parseInt(guessNumber) == 1) {
                odds = detailOdds.getO1();
                detailMoney.setM1(detailMoney.getM1().add(bet));
            } else if (Integer.parseInt(guessNumber) == 2) {
                odds = detailOdds.getO2();
                detailMoney.setM2(detailMoney.getM2().add(bet));
            } else {
                odds = BigDecimal.ZERO;
            }
            // 更新金额
            guessDetailMoney3Dao.save(detailMoney);
        }
        return odds;
    }

    /**
     * 修改所有中奖的下注的状态并进行奖金发放
     *
     * @param guessDetail 竞猜详情对象
     */
    @Async  // 异步处理
    public void guessRight(GuessDetail guessDetail) {

        // 获取当前竞猜详情的所有下注
        List<GuessBet> guessBetList = guessBetDao.findByGuessDetailId(guessDetail.getId());
        if (CollectionUtils.isEmpty(guessBetList)) {
            log.info("此次竞猜没有下注");
            return;
        }

        // 获取前两名
        String guessResult = guessDetail.getGuessResult();
        int first = Integer.parseInt(String.valueOf(guessResult.charAt(0)));
        int second = Integer.parseInt(String.valueOf(guessResult.charAt(1)));

        // 用户资产详情集合
        List<GuessUserAssetDetail> guessUserAssetDetailList = new ArrayList<>();
        // 奖金Map集合
        Map<Long, BigDecimal> bounsMap = new HashMap<>();

        // 修改所有当场竞猜详情的下注状态
        for (GuessBet guessBet : guessBetList) {
            // 获取当前下注赔率
            BigDecimal odds = guessBet.getBetOdds();
            // 设置竞猜状态（竞猜结果，1：猜中，2：未猜中）
            if (GuessModeEnum.MODE_1.getMode().equals(guessBet.getGuessMode())) {
                // 玩法一，取所有guessMode = 1的数据，与first参数相同则为胜利
                if (Integer.valueOf(guessBet.getGuessNumber()).equals(first)) {
                    // 计算实际奖金：投注金额 * 当前下注赔率
                    BigDecimal bouns = guessBet.getBetMoney().multiply(odds).setScale(5, BigDecimal.ROUND_DOWN);
                    Long userId = guessBet.getUserId();
                    BigDecimal totalBouns = bounsMap.get(userId);
                    // 未获取到value则初始化value
                    if (bounsMap.get(userId) == null) {
                        bounsMap.put(userId, bouns);
                    } else {
                        bounsMap.put(userId, totalBouns.add(bouns));
                    }
                    guessBet.setResultBouns(bouns);
                    guessBet.setBetStatus(GuessBetStatusEnum.GUESS_RIGHT.getCode());
                    // 创建用户资产详情对象，封装数据并存入用户资产详情集合
                    Date date = new Date();
                    GuessUserAssetDetail guessUserAssetDetail = new GuessUserAssetDetail();
                    guessUserAssetDetail.setCreateTime(date);
                    guessUserAssetDetail.setMoney(bouns);
                    guessUserAssetDetail.setProfit(BigDecimal.ZERO);
                    guessUserAssetDetail.setProfitExt(BigDecimal.ZERO);
                    guessUserAssetDetail.setTransType(TransType.GuessRight.getValue());
                    guessUserAssetDetail.setUpdateTime(date);
                    guessUserAssetDetail.setVersionType(appConfig.getVersion().getValue());
                    guessUserAssetDetail.setUserId(userId);
                    guessUserAssetDetailList.add(guessUserAssetDetail);
                } else {
                    guessBet.setBetStatus(GuessBetStatusEnum.NOT_GUESS_RIGHT.getCode());
                }
            } else if (GuessModeEnum.MODE_2.getMode().equals(guessBet.getGuessMode())) {
                // 玩法二，取所有guessMode = 2的数据，两个竞猜的数字与first、second参数相同则为胜利，不分先后
                Integer guessNumber1 = Integer.valueOf(String.valueOf(guessBet.getGuessNumber().charAt(0)));
                Integer guessNumber2 = Integer.valueOf(String.valueOf(guessBet.getGuessNumber().charAt(1)));
                if ((guessNumber1.equals(first) && guessNumber2.equals(second))
                        || (guessNumber2.equals(first) && guessNumber1.equals(second))) {
                    // 计算实际奖金：投注金额 * 当前下注赔率
                    BigDecimal bouns = guessBet.getBetMoney().multiply(odds).setScale(5, BigDecimal.ROUND_DOWN);
                    Long userId = guessBet.getUserId();
                    BigDecimal totalBouns = bounsMap.get(userId);
                    // 未获取到value则初始化value
                    if (bounsMap.get(userId) == null) {
                        bounsMap.put(userId, bouns);
                    } else {
                        bounsMap.put(userId, totalBouns.add(bouns));
                    }
                    guessBet.setResultBouns(bouns);
                    guessBet.setBetStatus(GuessBetStatusEnum.GUESS_RIGHT.getCode());
                    // 创建用户资产详情对象，封装数据并存入用户资产详情集合
                    Date date = new Date();
                    GuessUserAssetDetail guessUserAssetDetail = new GuessUserAssetDetail();
                    guessUserAssetDetail.setCreateTime(date);
                    guessUserAssetDetail.setMoney(bouns);
                    guessUserAssetDetail.setProfit(BigDecimal.ZERO);
                    guessUserAssetDetail.setProfitExt(BigDecimal.ZERO);
                    guessUserAssetDetail.setTransType(TransType.GuessRight.getValue());
                    guessUserAssetDetail.setUpdateTime(date);
                    guessUserAssetDetail.setVersionType(appConfig.getVersion().getValue());
                    guessUserAssetDetail.setUserId(userId);
                    guessUserAssetDetailList.add(guessUserAssetDetail);
                } else {
                    guessBet.setBetStatus(GuessBetStatusEnum.NOT_GUESS_RIGHT.getCode());
                }
            } else {
                // 玩法三，取所有guessMode = 3的数据，first为奇数则竞猜数字等于1的胜利，为偶数竞猜数字等于2的胜利
                Integer guessNumber = Integer.parseInt(guessBet.getGuessNumber());
                if (first % 2 != 0) {
                    if (guessNumber.equals(1)) {
                        // 计算实际奖金：投注金额 * 当前下注赔率
                        BigDecimal bouns = guessBet.getBetMoney().multiply(odds).setScale(5, BigDecimal.ROUND_DOWN);
                        Long userId = guessBet.getUserId();
                        BigDecimal totalBouns = bounsMap.get(userId);
                        // 未获取到value则初始化value
                        if (bounsMap.get(userId) == null) {
                            bounsMap.put(userId, bouns);
                        } else {
                            bounsMap.put(userId, totalBouns.add(bouns));
                        }
                        guessBet.setResultBouns(bouns);
                        guessBet.setBetStatus(GuessBetStatusEnum.GUESS_RIGHT.getCode());
                        // 创建用户资产详情对象，封装数据并存入用户资产详情集合
                        Date date = new Date();
                        GuessUserAssetDetail guessUserAssetDetail = new GuessUserAssetDetail();
                        guessUserAssetDetail.setCreateTime(date);
                        guessUserAssetDetail.setMoney(bouns);
                        guessUserAssetDetail.setProfit(BigDecimal.ZERO);
                        guessUserAssetDetail.setProfitExt(BigDecimal.ZERO);
                        guessUserAssetDetail.setTransType(TransType.GuessRight.getValue());
                        guessUserAssetDetail.setUpdateTime(date);
                        guessUserAssetDetail.setVersionType(appConfig.getVersion().getValue());
                        guessUserAssetDetail.setUserId(userId);
                        guessUserAssetDetailList.add(guessUserAssetDetail);
                    } else {
                        guessBet.setBetStatus(GuessBetStatusEnum.NOT_GUESS_RIGHT.getCode());
                    }
                } else {
                    if (guessNumber.equals(2)) {
                        // 计算实际奖金：投注金额 * 当前下注赔率
                        BigDecimal bouns = guessBet.getBetMoney().multiply(odds).setScale(5, BigDecimal.ROUND_DOWN);
                        Long userId = guessBet.getUserId();
                        BigDecimal totalBouns = bounsMap.get(userId);
                        // 未获取到value则初始化value
                        if (bounsMap.get(userId) == null) {
                            bounsMap.put(userId, bouns);
                        } else {
                            bounsMap.put(userId, totalBouns.add(bouns));
                        }
                        guessBet.setResultBouns(bouns);
                        guessBet.setBetStatus(GuessBetStatusEnum.GUESS_RIGHT.getCode());
                        // 创建用户资产详情对象，封装数据并存入用户资产详情集合
                        Date date = new Date();
                        GuessUserAssetDetail guessUserAssetDetail = new GuessUserAssetDetail();
                        guessUserAssetDetail.setCreateTime(date);
                        guessUserAssetDetail.setMoney(bouns);
                        guessUserAssetDetail.setProfit(BigDecimal.ZERO);
                        guessUserAssetDetail.setProfitExt(BigDecimal.ZERO);
                        guessUserAssetDetail.setTransType(TransType.GuessRight.getValue());
                        guessUserAssetDetail.setUpdateTime(date);
                        guessUserAssetDetail.setVersionType(appConfig.getVersion().getValue());
                        guessUserAssetDetail.setUserId(userId);
                        guessUserAssetDetailList.add(guessUserAssetDetail);
                    } else {
                        guessBet.setBetStatus(GuessBetStatusEnum.NOT_GUESS_RIGHT.getCode());
                    }
                }
            }
        }
        log.debug("用户总奖金Map: {}", bounsMap);
        // 修改下注状态
        guessBetDao.save(guessBetList);
        // 更新用户剩余余额
        List<Long> userIdList = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : bounsMap.entrySet()) {
            userIdList.add(entry.getKey());
        }
        // 查询用户资产
        List<GuessUserAsset> guessUserAssetList = guessUserAssetDao.findByUserIdIn(userIdList);
        for (GuessUserAsset guessUserAsset : guessUserAssetList) {
            // 将奖金加到剩余额上，然后重新计算totalAsset
            BigDecimal totalBouns = bounsMap.get(guessUserAsset.getUserId());
            guessUserAsset.setProfit(guessUserAsset.getProfit().add(totalBouns));
            guessUserAsset.setRemainAsset(guessUserAsset.getRemainAsset().add(totalBouns));
            guessUserAsset.setTotalAsset(guessUserAsset.getRemainAsset().add(guessUserAsset.getFrozenAsset()));
        }
        // 更新用户资产
        guessUserAssetDao.save(guessUserAssetList);
        // 批量添加用户资产详情
        guessUserAssetDetailDao.save(guessUserAssetDetailList);
    }

}
