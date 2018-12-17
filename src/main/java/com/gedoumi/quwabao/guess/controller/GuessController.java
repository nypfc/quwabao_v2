package com.gedoumi.quwabao.guess.controller;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.GuessDetailStatusEnum;
import com.gedoumi.quwabao.common.enums.GuessStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.guess.dataobj.form.BetForm;
import com.gedoumi.quwabao.guess.dataobj.model.*;
import com.gedoumi.quwabao.guess.dataobj.vo.GuessBetVO;
import com.gedoumi.quwabao.guess.dataobj.vo.GuessDetailStatusVO;
import com.gedoumi.quwabao.guess.dataobj.vo.GuessDetailVO;
import com.gedoumi.quwabao.guess.dataobj.vo.GuessOddsVO;
import com.gedoumi.quwabao.guess.service.*;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.gedoumi.quwabao.common.constants.ApiConstants.APP_GUESS;
import static com.gedoumi.quwabao.common.constants.Constants.DATETIME_FORMAT;

/**
 * 竞猜Controller
 *
 * @author Minced
 */
@RequestMapping(APP_GUESS)
@RestController
public class GuessController {

    @Resource
    private GuessService guessService;

    @Resource
    private GuessDetailService guessDetailService;

    @Resource
    private GuessBetService guessBetService;

    @Resource
    private GuessDetailOdds1Service guessDetailOdds1Service;

    @Resource
    private GuessDetailOdds2Service guessDetailOdds2Service;

    @Resource
    private GuessDetailOdds3Service guessDetailOdds3Service;

    /**
     * 获取竞猜状态
     *
     * @return ResponseObject
     */
    @GetMapping("/status")
    public ResponseObject guessStatus() {
        GuessDetailStatusVO statusVO = new GuessDetailStatusVO();
        // 获取最新的竞猜期
        Guess guess = guessService.getLatest();
        // 如果竞猜期为空，返回初始化状态，下场时间0
        if (guess == null) {
            statusVO.setNext("0");
            statusVO.setStatus(GuessDetailStatusEnum.INIT.getValue());
            return new ResponseObject<>(statusVO);
        }
        // 不为游戏期，设置状态为初始化状态
        if (!guess.getGuessStatus().equals(GuessStatusEnum.BEGINNING.getValue())) {
            // 如果下场开始时间小于当前时间，返回下场时间0
            Date startTime = guess.getStartTime();
            Date now = new Date();
            if (startTime.getTime() > now.getTime()) {
                statusVO.setNext(DATETIME_FORMAT.format(startTime));
            } else {
                statusVO.setNext("0");
            }
            statusVO.setStatus(GuessDetailStatusEnum.INIT.getValue());
            return new ResponseObject<>(statusVO);
        }
        // 查询竞猜详情状态，为空则设置状态为初始化
        GuessDetail guessDetail = guessDetailService.getLatest();
        if (guessDetail != null) {
            statusVO.setStatus(guessDetail.getGuessDetailStatus());
        } else {
            statusVO.setStatus(GuessDetailStatusEnum.INIT.getValue());
        }
        return new ResponseObject<>(statusVO);
    }

    /**
     * 获取三总玩法的竞猜赔率
     *
     * @return ResponseObject
     */
    @GetMapping("/odds")
    public ResponseObject odds() {
        // 获取正在下注期的竞猜详情
        GuessDetail guessDetail = Optional.ofNullable(guessDetailService.getLatest()).orElseThrow(() -> new BusinessException(CodeEnum.NotBetting));
        // 获取竞猜，计算剩余时间：结束的时间戳 - 当前时间的时间戳
        Guess guess = guessService.getById(guessDetail.getGuessId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(guessDetail.getStartTime());
        calendar.add(Calendar.SECOND, guess.getBetTime());
        int remainTime = (int) (calendar.getTime().getTime() / 1000 - new Date().getTime() / 1000);
        // 获取三种玩法赔率
        Long guessDetailId = guessDetail.getId();
        GuessDetailOdds1 odds1 = guessDetailOdds1Service.getGuessDetailOdds1(guessDetailId);
        GuessDetailOdds2 odds2 = guessDetailOdds2Service.getGuessDetailOdds2(guessDetailId);
        GuessDetailOdds3 odds3 = guessDetailOdds3Service.getGuessDetailOdds3(guessDetailId);
        // 封装返回对象
        GuessOddsVO guessOddsVO = new GuessOddsVO();
        GuessOddsVO.Odds1VO mode1 = guessOddsVO.getMode1();
        mode1.setO1(odds1.getO1());
        mode1.setO2(odds1.getO2());
        mode1.setO3(odds1.getO3());
        mode1.setO4(odds1.getO4());
        mode1.setO5(odds1.getO5());
        mode1.setO6(odds1.getO6());
        GuessOddsVO.Odds2VO mode2 = guessOddsVO.getMode2();
        mode2.setO12(odds2.getO12());
        mode2.setO13(odds2.getO13());
        mode2.setO14(odds2.getO14());
        mode2.setO15(odds2.getO15());
        mode2.setO16(odds2.getO16());
        mode2.setO23(odds2.getO23());
        mode2.setO24(odds2.getO24());
        mode2.setO25(odds2.getO25());
        mode2.setO26(odds2.getO26());
        mode2.setO34(odds2.getO34());
        mode2.setO35(odds2.getO35());
        mode2.setO36(odds2.getO36());
        mode2.setO45(odds2.getO45());
        mode2.setO46(odds2.getO46());
        mode2.setO56(odds2.getO56());
        GuessOddsVO.Odds3VO mode3 = guessOddsVO.getMode3();
        mode3.setO1(odds3.getO1());
        mode3.setO2(odds3.getO2());
        guessOddsVO.setRemain(remainTime);
        return new ResponseObject<>(guessOddsVO);
    }

    /**
     * 用户下注列表
     *
     * @return ResponseObject
     */
    @GetMapping("/bet/{page}/{rows}")
    public ResponseObject bets(@PathVariable String page, @PathVariable String rows) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 获取用户下注数据集合
        List<GuessBet> guessBets = guessBetService.getBets(user.getId(), page, rows);
        // 获取到竞猜详情的ID集合并查询竞猜详情
        Set<Long> detailIdSet = guessBets.stream().map(GuessBet::getGuessDetailId).collect(Collectors.toSet());
        List<GuessDetail> guessDetails = guessDetailService.getByIdIn(detailIdSet);
        // 遍历集合，如果竞猜ID相同，封装期号
        ArrayList<GuessBetVO> guessBetVOs = Lists.newArrayList();
        guessBets.forEach(guessBet -> guessDetails.forEach(guessDetail -> {
            if (guessBet.getGuessDetailId().equals(guessDetail.getId())) {
                GuessBetVO betVO = new GuessBetVO();
                betVO.setDate(guessBet.getBetTime());
                betVO.setIssueNumber(String.valueOf(guessDetail.getIssueNumber()));
                betVO.setGuessMode(String.valueOf(guessBet.getGuessMode()));
                betVO.setGuessNumber(String.valueOf(guessBet.getGuessNumber()));
                betVO.setBetTime(guessBet.getBetTime());
                betVO.setBetMoney(guessBet.getBetMoney().stripTrailingZeros().toPlainString());
                betVO.setBetOdds(guessBet.getBetOdds().stripTrailingZeros().toPlainString());
                betVO.setResultBouns(guessBet.getResultBouns().stripTrailingZeros().toPlainString());
                betVO.setBetStatus(guessBet.getBetStatus());
                guessBetVOs.add(betVO);
            }
        }));
        return new ResponseObject<>(guessBetVOs);
    }

    /**
     * 历史记录列表
     *
     * @param page 当前页
     * @param rows 每页数据量
     * @return ResponseObject
     */
    @GetMapping("/result/{page}/{rows}")
    public ResponseObject guessHistory(@PathVariable String page, @PathVariable String rows) {
        // 获取历史记录
        List<GuessDetail> guessDetails = guessDetailService.getGuessDetails(page, rows);
        // 遍历集合，封装返回数据
        List<GuessDetailVO> guessDetailVOs = guessDetails.stream().map(guessDetail -> {
            GuessDetailVO guessDetailVO = new GuessDetailVO();
            guessDetailVO.setDate(guessDetail.getStartTime());
            guessDetailVO.setIssueNumber(String.valueOf(guessDetail.getIssueNumber()));
            guessDetailVO.setGuessResult(guessDetail.getGuessResult());
            return guessDetailVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(guessDetailVOs);
    }

    /**
     * 用户下注
     *
     * @param betForm 下注参数
     * @return ResponseObject
     */
    @PostMapping("/bet")
    public ResponseObject bet(@RequestBody BetForm betForm) {
        guessBetService.bet(betForm);
        return new ResponseObject();
    }

}
