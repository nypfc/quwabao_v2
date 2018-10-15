package com.gedoumi.quwabao.guess.controller;

import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.GuessDetailStatusEnum;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.guess.dto.GuessBetDTO;
import com.gedoumi.quwabao.guess.dto.GuessOddsDTO;
import com.gedoumi.quwabao.guess.entity.GuessBet;
import com.gedoumi.quwabao.guess.entity.GuessDetail;
import com.gedoumi.quwabao.guess.form.BetForm;
import com.gedoumi.quwabao.guess.form.BetFormValidate;
import com.gedoumi.quwabao.guess.form.HistoryForm;
import com.gedoumi.quwabao.guess.service.GuessBetService;
import com.gedoumi.quwabao.guess.service.GuessDetailService;
import com.gedoumi.quwabao.guess.service.GuessService;
import com.gedoumi.quwabao.guess.vo.*;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 竞猜Controller
 * @author Minced
 */
@Slf4j
@RequestMapping("/v1/guess")
@RestController
public class GuessController {

    @Resource
    private GuessService guessService;
    @Resource
    private GuessDetailService guessDetailService;
    @Resource
    private GuessBetService guessBetService;

    /**
     * 获取三总玩法的竞猜赔率
     * @return ResponseObject
     */
    @PostMapping("/odds")
    public ResponseObject odds() {

        ResponseObject responseObject = new ResponseObject();
        // 获取正在下注期的竞猜详情
        GuessDetail guessDetail = guessDetailService.getStartedGuessDetail();
        // 未查询到下注期的竞猜详情，获取下场时间
        if (guessDetail == null) {
            Map<String, String> next = new HashMap<>();
            next.put("next", guessService.getNextTime());
            responseObject.setInfo(CodeEnum.NotBetting);
            responseObject.setData(next);
            return responseObject;
        }
        // 判断竞猜状态，不为下注期则返回
        Integer status = guessDetail.getGuessDetailStatus();
        if (!status.equals(GuessDetailStatusEnum.BETTING.getCode())) {
            if (status.equals(GuessDetailStatusEnum.GAMING.getCode())) {
                responseObject.setInfo(CodeEnum.Gaming);  // 返回游戏期
                Map<String, String> map = new HashMap<>();
                map.put("issue", guessDetail.getIssueNumber().toString());
                responseObject.setData(map);
                return responseObject;
            } else {
                responseObject.setInfo(CodeEnum.Bouns);  // 返回算奖期
                return responseObject;
            }
        }
        // 获取赔率各玩法赔率对象
        GuessOddsDTO oddsDTO = guessDetailService.getOdds(guessDetail.getId());
        // 获取下注剩余的时间
        Integer remainTime = guessService.getRemainTime(guessDetail);
        // 封装返回对象
        GuessProbabilityVO guessProbabilityVO = new GuessProbabilityVO();
        Odds1VO odds1VO = new Odds1VO();
        Odds2VO odds2VO = new Odds2VO();
        Odds3VO odds3VO = new Odds3VO();
        BeanUtils.copyProperties(oddsDTO.getGuessDetailOdds1(), odds1VO);
        BeanUtils.copyProperties(oddsDTO.getGuessDetailOdds2(), odds2VO);
        BeanUtils.copyProperties(oddsDTO.getGuessDetailOdds3(), odds3VO);
        guessProbabilityVO.setMode1(odds1VO);
        guessProbabilityVO.setMode2(odds2VO);
        guessProbabilityVO.setMode3(odds3VO);
        guessProbabilityVO.setRemain(remainTime);
        // 获取用户并设置用户余额
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        guessProbabilityVO.setUserRemainAsset(guessBetService.getUserRemainAsset(user.getId()));

        responseObject.setSuccess();
        responseObject.setData(guessProbabilityVO);
        return responseObject;
    }

    /**
     * 用户下注列表
     * @return ResponseObject
     */
    @PostMapping("/mylist")
    public ResponseObject userBetList() {
        // 获取用户
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        // 获取用户下注数据集合
        List<GuessBetDTO> guessBetDTOList = guessBetService.getUserBetList(user.getId());
        // 遍历集合，封装返回数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<GuessBetVO> betVOList = guessBetDTOList.stream().map(guessBetDTO -> {
            GuessBetVO betVO = new GuessBetVO();
            betVO.setDate(sdf.format(guessBetDTO.getBetTime()));
            betVO.setIssueNumber(guessBetDTO.getIssueNumber());
            betVO.setGuessMode(String.valueOf(guessBetDTO.getGuessMode()));
            betVO.setGuessNumber(guessBetDTO.getGuessNumber());
            betVO.setBetTime(guessBetDTO.getBetTime());
            betVO.setBetMoney(String.valueOf(guessBetDTO.getBetMoney()));
            betVO.setBetOdds(String.valueOf(guessBetDTO.getBetOdds()));
            betVO.setResultBouns(String.valueOf(guessBetDTO.getResultBouns()));
            betVO.setBetStatus(String.valueOf(guessBetDTO.getBetStatus()));
            return betVO;
        }).collect(Collectors.toList());

        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        responseObject.setData(betVOList);
        return responseObject;
    }

    /**
     * 历史记录列表
     * @return ResponseObject
     */
    @PostMapping("/history")
    public ResponseObject guessHistory(@RequestBody HistoryForm historyForm) {
        // 获取历史记录
        List<GuessDetail> historyList = guessDetailService.findHistoryList(historyForm.getPage(), historyForm.getSize());
        // 遍历集合，封装返回数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<GuessHistoryVO> historyVOList = historyList.stream().map(guessDetail -> {
            GuessHistoryVO historyVO = new GuessHistoryVO();
            historyVO.setDate(sdf.format(guessDetail.getStartTime()));
            historyVO.setIssueNumber(String.valueOf(guessDetail.getIssueNumber()));
            historyVO.setGuessResult(guessDetail.getGuessResult());
            // 获取第一名数字，如果是奇数，则白队胜利，否则黑队胜利
            String first = String.valueOf(guessDetail.getGuessResult().charAt(0));
            historyVO.setSuccessTeam(Integer.parseInt(first) % 2 != 0 ? "白" : "黑");
            return historyVO;
        }).collect(Collectors.toList());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        responseObject.setData(historyVOList);
        return responseObject;
    }

    /**
     * 用户下注
     * @param betForm 下注参数
     * @return ResponseObject
     */
    @Transactional
    @PostMapping("/bet")
    public ResponseObject bet(@RequestBody BetForm betForm) {
        // 获取参数
        Double bet = betForm.getBet();
        Integer guessMode = betForm.getGuessMode();
        String guessNumber = betForm.getGuessNumber();

        ResponseObject responseObject = new ResponseObject();

        // 参数验证
        String message = BetFormValidate.validate(bet, guessMode, guessNumber);
        if (message != null) {
            log.debug(message);
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
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        BigDecimal bet_ = new BigDecimal(bet).setScale(5, BigDecimal.ROUND_DOWN);  // double转BigDecimal
        if (!guessBetService.updateUserAsset(user.getId(), bet_)) {
            responseObject.setInfo(CodeEnum.RemainAssetError);
            return responseObject;
        }
        // 创建下注记录
        GuessBet guessBet = new GuessBet();
        guessBet.setUserId(user.getId());
        guessBet.setGuessDetailId(guessDetail.getId());
        guessBet.setBetTime(new Date());
        guessBet.setGuessMode(guessMode);
        guessBet.setBetMoney(bet_);
        guessBet.setGuessNumber(guessNumber);
        guessBet.setBetOdds(guessBetService.updateGuessDetailMoney(guessBet));  // 更新对应选项的投注金额，并获得其赔率
        guessBetService.save(guessBet);
        // 创建竞猜用户资产详情
        guessBetService.createUserAssetDetail(user.getId(), bet_, BigDecimal.ZERO, TransType.Bet.getValue());

        responseObject.setSuccess();
        return responseObject;
    }

}
