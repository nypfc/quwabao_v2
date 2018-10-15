package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.common.enums.GuessDetailStatusEnum;
import com.gedoumi.quwabao.guess.dao.*;
import com.gedoumi.quwabao.guess.dto.GuessOddsDTO;
import com.gedoumi.quwabao.guess.entity.*;
import com.gedoumi.quwabao.util.GuessProbabilityUtil;
import com.gedoumi.quwabao.util.ListUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 竞猜详情Service
 * @author Minced
 */
@Slf4j
@Service
public class GuessDetailService {

    @Resource
    private GuessDetailDao guessDetailDao;
    @Resource
    private GuessDetailMoney1Dao guessDetailMoney1Dao;
    @Resource
    private GuessDetailMoney2Dao guessDetailMoney2Dao;
    @Resource
    private GuessDetailMoney3Dao guessDetailMoney3Dao;
    @Resource
    private GuessDetailOdds1Dao guessDetailOdds1Dao;
    @Resource
    private GuessDetailOdds2Dao guessDetailOdds2Dao;
    @Resource
    private GuessDetailOdds3Dao guessDetailOdds3Dao;
    @Resource
    private GuessDetailReturnDao guessDetailReturnDao;

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 竞猜详情
     */
    public GuessDetail findById(Long guessDetailId) {
        return guessDetailDao.findById(guessDetailId).get();
    }

    /**
     * 获取未结束的竞猜详情
     * @return 竞猜详情对象
     */
    public GuessDetail getStartedGuessDetail() {
        // 查询第一条未结束的竞猜详情
        PageRequest pageRequest = new PageRequest(0, 1);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(GuessDetailStatusEnum.BETTING.getCode());
        statusList.add(GuessDetailStatusEnum.GAMING.getCode());
        statusList.add(GuessDetailStatusEnum.BOUNS.getCode());
        List<GuessDetail> guessDetailList = guessDetailDao.findByGuessDetailStatusIn(statusList, pageRequest);
        // 空集合返回null
        if (CollectionUtils.isEmpty(guessDetailList)) return null;
        // 不为空返回第一个对象
        return guessDetailList.get(0);
    }

    /**
     * 获取三个玩法的赔率
     * @param guessDetailId 竞猜详情ID
     * @return 赔率DTO
     */
    public GuessOddsDTO getOdds(Long guessDetailId) {
        GuessDetailOdds1 odds1 = guessDetailOdds1Dao.findByGuessDetailId(guessDetailId);
        GuessDetailOdds2 odds2 = guessDetailOdds2Dao.findByGuessDetailId(guessDetailId);
        GuessDetailOdds3 odds3 = guessDetailOdds3Dao.findByGuessDetailId(guessDetailId);
        GuessOddsDTO guessOddsDTO = new GuessOddsDTO();
        guessOddsDTO.setGuessDetailOdds1(odds1);
        guessOddsDTO.setGuessDetailOdds2(odds2);
        guessOddsDTO.setGuessDetailOdds3(odds3);
        return guessOddsDTO;
    }

    /**
     * 获取结束的竞猜详情的列表
     * @param page 当前页
     * @param size 每页数据量
     * @return 竞猜详情列表
     */
    public List<GuessDetail> findHistoryList(Integer page, Integer size) {
        PageRequest pageRequest = new PageRequest(page - 1, size);
        return guessDetailDao.findByGuessDetailStatusOrderByStartTimeDesc(GuessDetailStatusEnum.FINISHED.getCode(), pageRequest);
    }

    /**
     * 获取正在下注期的竞猜详情
     * @return 竞猜详情对象
     */
    public GuessDetail getBettingGuessDetail() {
        // 查询第一条未结束的竞猜详情
        PageRequest pageRequest = new PageRequest(0, 1);
        List<GuessDetail> guessDetailList = guessDetailDao.findByGuessDetailStatus(GuessDetailStatusEnum.BETTING.getCode(), pageRequest);
        // 空集合返回null
        if (CollectionUtils.isEmpty(guessDetailList)) return null;
        // 不为空返回第一个对象
        return guessDetailList.get(0);
    }

    /**
     * 创建/更新竞猜详情
     * @param guessDetail 竞猜详情对象
     * @return 竞猜详情对象
     */
    @Transactional
    public GuessDetail save(GuessDetail guessDetail) {
        return guessDetailDao.save(guessDetail);
    }

    /**
     * 创建新的竞猜详情、各玩法的赔率与各玩法的投注金额
     * @param guess 竞猜对象
     * @param startDateTime 开始时间
     * @return 竞猜详情ID
     */
    @Transactional
    public Long createGuessDetailAndOddsAndMoney(Guess guess, Date startDateTime) {
        // 查询当天的竞猜详情数量，设置期次 = 当天详情数量 + 1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        try {
            start = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 当前日期 + 1查询
        Calendar calendar = Calendar.getInstance();
        assert start != null;
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Integer dayCount = guessDetailDao.countByStartTimeBetween(start, calendar.getTime());
        // 创建竞猜详情
        GuessDetail guessDetail = new GuessDetail();
        guessDetail.setGuessId(guess.getId());
        guessDetail.setStartTime(startDateTime);
        guessDetail.setIssueNumber(dayCount + 1);
        guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.BETTING.getCode());
        GuessDetail result = guessDetailDao.save(guessDetail);
        // 获取ID
        Long guessDetailId = result.getId();
        // 创建玩法一赔率
        BigDecimal odds = guess.getOdds();
        List<BigDecimal> odds1List = GuessProbabilityUtil.mode1(odds, 10, 25);  // 获取玩法一赔率集合
        GuessDetailOdds1 guessDetailOdds1 = new GuessDetailOdds1();
        guessDetailOdds1.setGuessDetailId(guessDetailId);
        guessDetailOdds1.setO1(odds1List.get(0));
        guessDetailOdds1.setO2(odds1List.get(1));
        guessDetailOdds1.setO3(odds1List.get(2));
        guessDetailOdds1.setO4(odds1List.get(3));
        guessDetailOdds1.setO5(odds1List.get(4));
        guessDetailOdds1.setO6(odds1List.get(5));
        guessDetailOdds1Dao.save(guessDetailOdds1);
        // 创建玩法二赔率
        List<BigDecimal> odds2List = GuessProbabilityUtil.mode2(odds, 5, 10);  // 获取玩法二赔率集合
        GuessDetailOdds2 guessDetailOdds2 = new GuessDetailOdds2();
        guessDetailOdds2.setGuessDetailId(guessDetailId);
        guessDetailOdds2.setO12(odds2List.get(0));
        guessDetailOdds2.setO13(odds2List.get(1));
        guessDetailOdds2.setO14(odds2List.get(2));
        guessDetailOdds2.setO15(odds2List.get(3));
        guessDetailOdds2.setO16(odds2List.get(4));
        guessDetailOdds2.setO23(odds2List.get(5));
        guessDetailOdds2.setO24(odds2List.get(6));
        guessDetailOdds2.setO25(odds2List.get(7));
        guessDetailOdds2.setO26(odds2List.get(8));
        guessDetailOdds2.setO34(odds2List.get(9));
        guessDetailOdds2.setO35(odds2List.get(10));
        guessDetailOdds2.setO36(odds2List.get(11));
        guessDetailOdds2.setO45(odds2List.get(12));
        guessDetailOdds2.setO46(odds2List.get(13));
        guessDetailOdds2.setO56(odds2List.get(14));
        guessDetailOdds2Dao.save(guessDetailOdds2);
        // 创建玩法三赔率
        List<BigDecimal> odds3List = GuessProbabilityUtil.mode3(odds, 45, 55);  // 获取玩法三赔率集合
        GuessDetailOdds3 guessDetailOdds3 = new GuessDetailOdds3();
        guessDetailOdds3.setGuessDetailId(guessDetailId);
        guessDetailOdds3.setO1(odds3List.get(0));
        guessDetailOdds3.setO2(odds3List.get(1));
        guessDetailOdds3Dao.save(guessDetailOdds3);
        // 创建玩法一投注金额
        GuessDetailMoney1 guessDetailMoney1 = new GuessDetailMoney1();
        guessDetailMoney1.setGuessDetailId(guessDetailId);
        guessDetailMoney1Dao.save(guessDetailMoney1);
        // 创建玩法二投注金额
        GuessDetailMoney2 guessDetailMoney2 = new GuessDetailMoney2();
        guessDetailMoney2.setGuessDetailId(guessDetailId);
        guessDetailMoney2Dao.save(guessDetailMoney2);
        // 创建玩法三投注金额
        GuessDetailMoney3 guessDetailMoney3 = new GuessDetailMoney3();
        guessDetailMoney3.setGuessDetailId(guessDetailId);
        guessDetailMoney3Dao.save(guessDetailMoney3);
        // 创建实际返奖率
        GuessDetailReturn guessDetailReturn = new GuessDetailReturn();
        guessDetailReturn.setGuessDetailId(guessDetailId);
        guessDetailReturnDao.save(guessDetailReturn);
        return guessDetailId;
    }

    /**
     * 获取矿车排名
     * @param guessDetail 竞猜详情
     * @param return_rate 返奖率
     * @return 名次排序字符串
     */
    public String getRanking(GuessDetail guessDetail, BigDecimal return_rate) {

        Long guessDetailId = guessDetail.getId();

        // 设置1-6名集合
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);

        // 获取各玩法赔率
        GuessDetailOdds1 guessDetailOdds1 = guessDetailOdds1Dao.findByGuessDetailId(guessDetailId);
        GuessDetailOdds2 guessDetailOdds2 = guessDetailOdds2Dao.findByGuessDetailId(guessDetailId);
        GuessDetailOdds3 guessDetailOdds3 = guessDetailOdds3Dao.findByGuessDetailId(guessDetailId);

        // 玩法一6个赔率
        BigDecimal oa1 = guessDetailOdds1.getO1();
        BigDecimal oa2 = guessDetailOdds1.getO2();
        BigDecimal oa3 = guessDetailOdds1.getO3();
        BigDecimal oa4 = guessDetailOdds1.getO4();
        BigDecimal oa5 = guessDetailOdds1.getO5();
        BigDecimal oa6 = guessDetailOdds1.getO6();
        // 玩法二15个赔率
        BigDecimal ob12 = guessDetailOdds2.getO12();
        BigDecimal ob13 = guessDetailOdds2.getO13();
        BigDecimal ob14 = guessDetailOdds2.getO14();
        BigDecimal ob15 = guessDetailOdds2.getO15();
        BigDecimal ob16 = guessDetailOdds2.getO16();
        BigDecimal ob23 = guessDetailOdds2.getO23();
        BigDecimal ob24 = guessDetailOdds2.getO24();
        BigDecimal ob25 = guessDetailOdds2.getO25();
        BigDecimal ob26 = guessDetailOdds2.getO26();
        BigDecimal ob34 = guessDetailOdds2.getO34();
        BigDecimal ob35 = guessDetailOdds2.getO35();
        BigDecimal ob36 = guessDetailOdds2.getO36();
        BigDecimal ob45 = guessDetailOdds2.getO45();
        BigDecimal ob46 = guessDetailOdds2.getO46();
        BigDecimal ob56 = guessDetailOdds2.getO56();
        // 玩法三2个赔率
        BigDecimal oc1 = guessDetailOdds3.getO1();
        BigDecimal oc2 = guessDetailOdds3.getO2();

        // 获取各玩法投注金额
        GuessDetailMoney1 guessDetailMoney1 = guessDetailMoney1Dao.findByGuessDetailId(guessDetailId);
        GuessDetailMoney2 guessDetailMoney2 = guessDetailMoney2Dao.findByGuessDetailId(guessDetailId);
        GuessDetailMoney3 guessDetailMoney3 = guessDetailMoney3Dao.findByGuessDetailId(guessDetailId);

        // 玩法一6个下注金额
        BigDecimal ma1 = guessDetailMoney1.getM1();
        BigDecimal ma2 = guessDetailMoney1.getM2();
        BigDecimal ma3 = guessDetailMoney1.getM3();
        BigDecimal ma4 = guessDetailMoney1.getM4();
        BigDecimal ma5 = guessDetailMoney1.getM5();
        BigDecimal ma6 = guessDetailMoney1.getM6();
        BigDecimal ma = ma1.add(ma2).add(ma3).add(ma4).add(ma5).add(ma6);
        // 如果有下注，更新金额
        if (ma.compareTo(BigDecimal.ZERO) != 0) {
            guessDetailMoney1.setTotal(ma);
            guessDetailMoney1Dao.save(guessDetailMoney1);
        }
        // 玩法二15个下注金额
        BigDecimal mb12 = guessDetailMoney2.getM12();
        BigDecimal mb13 = guessDetailMoney2.getM13();
        BigDecimal mb14 = guessDetailMoney2.getM14();
        BigDecimal mb15 = guessDetailMoney2.getM15();
        BigDecimal mb16 = guessDetailMoney2.getM16();
        BigDecimal mb23 = guessDetailMoney2.getM23();
        BigDecimal mb24 = guessDetailMoney2.getM24();
        BigDecimal mb25 = guessDetailMoney2.getM25();
        BigDecimal mb26 = guessDetailMoney2.getM26();
        BigDecimal mb34 = guessDetailMoney2.getM34();
        BigDecimal mb35 = guessDetailMoney2.getM35();
        BigDecimal mb36 = guessDetailMoney2.getM36();
        BigDecimal mb45 = guessDetailMoney2.getM45();
        BigDecimal mb46 = guessDetailMoney2.getM46();
        BigDecimal mb56 = guessDetailMoney2.getM56();
        BigDecimal mb = mb12.add(mb13).add(mb14).add(mb15).add(mb16).add(mb23).add(mb24).add(mb25).add(mb26)
                .add(mb34).add(mb35).add(mb36).add(mb45).add(mb46).add(mb56);
        // 如果有下注，更新金额
        if (mb.compareTo(BigDecimal.ZERO) != 0) {
            guessDetailMoney2.setTotal(mb);
            guessDetailMoney2Dao.save(guessDetailMoney2);
        }
        // 玩法三2个下注金额
        BigDecimal mc1 = guessDetailMoney3.getM1();
        BigDecimal mc2 = guessDetailMoney3.getM2();
        BigDecimal mc = mc1.add(mc2);
        // 如果有下注，更新金额
        if (mc.compareTo(BigDecimal.ZERO) != 0) {
            guessDetailMoney3.setTotal(mc);
            guessDetailMoney3Dao.save(guessDetailMoney3);
        }
        // 总投注金额
        BigDecimal mTotal = ma.add(mb).add(mc);
        log.debug("{}", mTotal);
        // 真实返奖率
        BigDecimal real_return_rate;
        // 判如果总金额为0，没有下注，随机名次；不为0，计算排名
        if (mTotal.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("此次竞猜没有下注");
            // 打乱集合顺序
            Collections.shuffle(list);
            // 真实返奖率为0
            real_return_rate = BigDecimal.ZERO;

        } else {
            // 实际返奖率
            GuessDetailReturn guessDetailReturn = guessDetailReturnDao.findByGuessDetailId(guessDetailId);
            // 结果集合
            LinkedHashMap<String, BigDecimal> result = new LinkedHashMap<>();
            // 1号冠军
            BigDecimal t12 = oa1.multiply(ma1).add(ob12.multiply(mb12)).add(oc1.multiply(mc1));
            BigDecimal t13 = oa1.multiply(ma1).add(ob13.multiply(mb13)).add(oc1.multiply(mc1));
            BigDecimal t14 = oa1.multiply(ma1).add(ob14.multiply(mb14)).add(oc1.multiply(mc1));
            BigDecimal t15 = oa1.multiply(ma1).add(ob15.multiply(mb15)).add(oc1.multiply(mc1));
            BigDecimal t16 = oa1.multiply(ma1).add(ob16.multiply(mb16)).add(oc1.multiply(mc1));
            BigDecimal r12 = t12.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r13 = t13.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r14 = t14.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r15 = t15.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r16 = t16.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("12", r12);
            result.put("13", r13);
            result.put("14", r14);
            result.put("15", r15);
            result.put("16", r16);
            // 2号冠军
            BigDecimal t21 = oa2.multiply(ma2).add(ob12.multiply(mb12)).add(oc2.multiply(mc2));
            BigDecimal t23 = oa2.multiply(ma2).add(ob23.multiply(mb23)).add(oc2.multiply(mc2));
            BigDecimal t24 = oa2.multiply(ma2).add(ob24.multiply(mb24)).add(oc2.multiply(mc2));
            BigDecimal t25 = oa2.multiply(ma2).add(ob25.multiply(mb25)).add(oc2.multiply(mc2));
            BigDecimal t26 = oa2.multiply(ma2).add(ob26.multiply(mb26)).add(oc2.multiply(mc2));
            BigDecimal r21 = t21.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r23 = t23.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r24 = t24.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r25 = t25.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r26 = t26.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("21", r21);
            result.put("23", r23);
            result.put("24", r24);
            result.put("25", r25);
            result.put("26", r26);
            // 3号冠军
            BigDecimal t31 = oa3.multiply(ma3).add(ob13.multiply(mb13)).add(oc1.multiply(mc1));
            BigDecimal t32 = oa3.multiply(ma3).add(ob23.multiply(mb23)).add(oc1.multiply(mc1));
            BigDecimal t34 = oa3.multiply(ma3).add(ob34.multiply(mb34)).add(oc1.multiply(mc1));
            BigDecimal t35 = oa3.multiply(ma3).add(ob35.multiply(mb35)).add(oc1.multiply(mc1));
            BigDecimal t36 = oa3.multiply(ma3).add(ob36.multiply(mb36)).add(oc1.multiply(mc1));
            BigDecimal r31 = t31.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r32 = t32.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r34 = t34.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r35 = t35.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r36 = t36.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("31", r31);
            result.put("32", r32);
            result.put("34", r34);
            result.put("35", r35);
            result.put("36", r36);
            // 4号冠军
            BigDecimal t41 = oa4.multiply(ma4).add(ob14.multiply(mb14)).add(oc2.multiply(mc2));
            BigDecimal t42 = oa4.multiply(ma4).add(ob24.multiply(mb24)).add(oc2.multiply(mc2));
            BigDecimal t43 = oa4.multiply(ma4).add(ob34.multiply(mb34)).add(oc2.multiply(mc2));
            BigDecimal t45 = oa4.multiply(ma4).add(ob45.multiply(mb45)).add(oc2.multiply(mc2));
            BigDecimal t46 = oa4.multiply(ma4).add(ob46.multiply(mb46)).add(oc2.multiply(mc2));
            BigDecimal r41 = t41.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r42 = t42.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r43 = t43.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r45 = t45.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r46 = t46.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("41", r41);
            result.put("42", r42);
            result.put("43", r43);
            result.put("45", r45);
            result.put("46", r46);
            // 5号冠军
            BigDecimal t51 = oa5.multiply(ma5).add(ob15.multiply(mb15)).add(oc1.multiply(mc1));
            BigDecimal t52 = oa5.multiply(ma5).add(ob25.multiply(mb25)).add(oc1.multiply(mc1));
            BigDecimal t53 = oa5.multiply(ma5).add(ob35.multiply(mb35)).add(oc1.multiply(mc1));
            BigDecimal t54 = oa5.multiply(ma5).add(ob45.multiply(mb45)).add(oc1.multiply(mc1));
            BigDecimal t56 = oa5.multiply(ma5).add(ob56.multiply(mb56)).add(oc1.multiply(mc1));
            BigDecimal r51 = t51.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r52 = t52.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r53 = t53.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r54 = t54.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r56 = t56.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("51", r51);
            result.put("52", r52);
            result.put("53", r53);
            result.put("54", r54);
            result.put("56", r56);
            // 6号冠军
            BigDecimal t61 = oa6.multiply(ma6).add(ob16.multiply(mb16)).add(oc2.multiply(mc2));
            BigDecimal t62 = oa6.multiply(ma6).add(ob26.multiply(mb26)).add(oc2.multiply(mc2));
            BigDecimal t63 = oa6.multiply(ma6).add(ob36.multiply(mb36)).add(oc2.multiply(mc2));
            BigDecimal t64 = oa6.multiply(ma6).add(ob46.multiply(mb46)).add(oc2.multiply(mc2));
            BigDecimal t65 = oa6.multiply(ma6).add(ob56.multiply(mb56)).add(oc2.multiply(mc2));
            BigDecimal r61 = t61.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r62 = t62.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r63 = t63.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r64 = t64.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal r65 = t65.divide(mTotal, 2, BigDecimal.ROUND_HALF_UP);
            result.put("61", r61);
            result.put("62", r62);
            result.put("63", r63);
            result.put("64", r64);
            result.put("65", r65);

            // 更新实际返奖率
            guessDetailReturn.setR12(r12);
            guessDetailReturn.setR13(r13);
            guessDetailReturn.setR14(r14);
            guessDetailReturn.setR15(r15);
            guessDetailReturn.setR16(r16);
            guessDetailReturn.setR21(r21);
            guessDetailReturn.setR23(r23);
            guessDetailReturn.setR24(r24);
            guessDetailReturn.setR25(r25);
            guessDetailReturn.setR26(r26);
            guessDetailReturn.setR31(r31);
            guessDetailReturn.setR32(r32);
            guessDetailReturn.setR34(r34);
            guessDetailReturn.setR35(r35);
            guessDetailReturn.setR36(r36);
            guessDetailReturn.setR41(r41);
            guessDetailReturn.setR42(r42);
            guessDetailReturn.setR43(r43);
            guessDetailReturn.setR45(r45);
            guessDetailReturn.setR46(r46);
            guessDetailReturn.setR51(r51);
            guessDetailReturn.setR52(r52);
            guessDetailReturn.setR53(r53);
            guessDetailReturn.setR54(r54);
            guessDetailReturn.setR56(r56);
            guessDetailReturn.setR61(r61);
            guessDetailReturn.setR62(r62);
            guessDetailReturn.setR63(r63);
            guessDetailReturn.setR64(r64);
            guessDetailReturn.setR65(r65);
            guessDetailReturnDao.save(guessDetailReturn);

            // 实际返奖率与预算返奖率差，分正数和负数Map
            Map<BigDecimal, String> positiveNumberList = new HashMap<>();
            Map<BigDecimal, String> negativeNumberList = new HashMap<>();

            // 计算实际返奖率与真实返奖率的差，大于0的存入正数集合，小于等于0的存入负数集合
            for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
                String key = entry.getKey();
                BigDecimal value = entry.getValue();
                log.debug("Key: {}, Value: {}", key, entry.getValue());
                BigDecimal diff = value.subtract(return_rate).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                if (diff.compareTo(BigDecimal.ZERO) <= 0) {
                    negativeNumberList.put(diff, key);
                } else {
                    positiveNumberList.put(diff, key);
                }
            }
            log.debug("正数集合：{}", positiveNumberList);
            log.debug("负数集合：{}", negativeNumberList);

            /*
                先将两个Map的Key（差值）存入两个集合，分正负
                获取负数集合的最大值，如果负数集合没有元素，则获取正数集合的最小值
                得到差值（key）后获取前两名
             */
            String ranking12;
            BigDecimal diff;

            List<BigDecimal> collect1 = positiveNumberList.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            List<BigDecimal> collect2 = negativeNumberList.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            log.debug("{}", collect1);
            log.debug("{}", collect2);

            if (CollectionUtils.isEmpty(negativeNumberList)) {
                diff = Collections.min(collect1);
                ranking12 = positiveNumberList.get(diff);
            } else {
                diff = Collections.max(collect2);
                ranking12 = negativeNumberList.get(diff);
            }

            // 计算真实返奖率
            real_return_rate = return_rate.add(diff).setScale(2, BigDecimal.ROUND_DOWN);
            log.debug("ranking12: {}", ranking12);

            // 打乱集合顺序
            Collections.shuffle(list);
            // 获取一二名的位置
            int first = Integer.parseInt(String.valueOf(ranking12.charAt(0)));
            int second = Integer.parseInt(String.valueOf(ranking12.charAt(1)));
            // 如果一二名的位置不对，则进行调换
            int i = list.indexOf(first);
            if (i != 0) ListUtil.swap(list, i, 0);
            int j = list.indexOf(second);
            if (j != 1) ListUtil.swap(list, j, 1);
        }
        // 更新排名、真实返奖率、总奖金、真实奖金和竞猜详情状态
        String ranking = String.valueOf(list.get(0)) +
                list.get(1) +
                list.get(2) +
                list.get(3) +
                list.get(4) +
                list.get(5);
        guessDetail.setGuessResult(ranking);
        guessDetail.setGuessRealReturn(real_return_rate);
        guessDetail.setTotalBouns(mTotal);
        guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.GAMING.getCode());  // 将状态从下注期改为游戏期
        guessDetailDao.save(guessDetail);

        return ranking;
    }

}
