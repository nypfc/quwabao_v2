package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.common.enums.GuessStatusEnum;
import com.gedoumi.quwabao.guess.dao.GuessDao;
import com.gedoumi.quwabao.guess.entity.Guess;
import com.gedoumi.quwabao.guess.entity.GuessDetail;
import com.gedoumi.quwabao.guess.vo.GuessNotityVO;
import com.gedoumi.quwabao.guess.websocket.GuessWebSocketHandler;
import com.gedoumi.quwabao.util.JsonUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 竞猜时间段Service
 * @author Minced
 */
@Service
public class GuessService {

    @Resource
    private GuessDao guessDao;

    @Resource
    private GuessWebSocketHandler guessWebSocketHandler;

    /**
     * 根据竞猜期ID查询
     * @param guessId 竞猜期ID
     * @return 竞猜期
     */
    public Guess findById(Long guessId) {
        return guessDao.findById(guessId).get();
    }

    /**
     * 获取下场时间
     * @return 下场时间的时间戳
     */
    public String getNextTime() {
        // 查询第一条竞猜期
        PageRequest pageRequest = new PageRequest(0, 1);
        List<Guess> guessList = guessDao.findByGuessStatus(GuessStatusEnum.NOT_STARTED.getCode(), pageRequest);
        // 未查询到返回0
        if (CollectionUtils.isEmpty(guessList)) return "0";
        // 查询到下一期返回第一条数据的开始时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(guessList.get(0).getStartTime());
    }

    /**
     * 获取当前下注期的剩余时间
     * @param guessDetail 竞猜详情
     * @return 剩余时间（单位：秒）
     */
    public Integer getRemainTime(GuessDetail guessDetail) {
        // 获取竞猜
        Guess guess = guessDao.findById(guessDetail.getGuessId()).get();
        // 根据下注时间获取到结束的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(guessDetail.getStartTime());
        calendar.add(Calendar.SECOND, guess.getBetTime());
        // 计算剩余时间：结束的时间戳 - 当前时间的时间戳
        return (int) (calendar.getTime().getTime() / 1000 - new Date().getTime() / 1000);
    }

    /**
     * 根据竞猜期开始时间查询竞猜期
     * @param startDateTime 开始时间
     * @return 竞猜期对象
     */
    public Guess findByStartTime(Date startDateTime) {
        return guessDao.findByStartTime(startDateTime);
    }

    /**
     * 创建/更新竞猜期
     * @param guess 竞猜期对象
     * @return 竞猜期对象
     */
    @Transactional
    public Guess save(Guess guess) {
        return guessDao.save(guess);
    }

    /**
     * 向前端发送WebSocket通知
     * @param typeEnum 通知类型枚举
     * @param data 数据
     */
    public void sendMessage(GuessNotityTypeEnum typeEnum, Object data) {
        GuessNotityVO guessNotityVO = new GuessNotityVO();
        guessNotityVO.setType(typeEnum.getType());
        guessNotityVO.setMessage(typeEnum.getMessage());
        guessNotityVO.setData(data);
        String resultJson = JsonUtil.objectToJson(guessNotityVO);
        assert resultJson != null;
        guessWebSocketHandler.sendMessageToUsers(new TextMessage(resultJson));
    }

}
