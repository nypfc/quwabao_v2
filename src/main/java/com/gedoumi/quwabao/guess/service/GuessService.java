package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.guess.dataobj.model.Guess;
import com.gedoumi.quwabao.guess.mapper.GuessMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 竞猜时间段Service
 *
 * @author Minced
 */
@Service
public class GuessService {

    @Resource
    private GuessMapper guessMapper;

//    @Resource
//    private GuessWebSocketHandler guessWebSocketHandler;

    /**
     * 根据竞猜期ID查询
     *
     * @param guessId 竞猜期ID
     * @return 竞猜期
     */
    public Guess getById(Long guessId) {
        return guessMapper.selectById(guessId);
    }

    /**
     * 获取最新的竞猜期
     *
     * @return 竞猜期
     */
    public Guess getLatest() {
        return guessMapper.selectLatest();
    }

//    /**
//     * 根据竞猜期开始时间查询竞猜期
//     *
//     * @param startDateTime 开始时间
//     * @return 竞猜期对象
//     */
//    public Guess findByStartTime(Date startDateTime) {
//        return guessMapper.findByStartTime(startDateTime);
//    }
//
//
//    @Transactional(rollbackFor = Exception.class)
//    public void schedule() {
//        // 查询竞猜期，未查询到则直接返回
//        Guess guess = guessService.findByStartTime(sdf.parse(sdf.format(startDateTime)));
//        if (guess == null) return;
//        // 修改竞猜状态
//        guess.setGuessStatus(GuessStatusEnum.BEGINNING.getValue());
//        guessService.save(guess);
//        // 创建新的竞猜详情、各玩法的赔率与各玩法的投注金额，获取返回的竞猜详情ID
//        Long guessDetailId = guessDetailService.createGuessDetailAndOddsAndMoney(guess, startDateTime);
//        // 设置Redis，过期时间从Guess对象中获取
//        redisCache.setExpireKeyValueData("guessDetail-" + guessDetailId, null, (long) guess.getBetTime(), TimeUnit.SECONDS);
//        // 给前端发送投注开始的通知
//        guessService.sendMessage(GuessNotityTypeEnum.BET_START, null);
//    }
//
//    /**
//     * 向前端发送WebSocket通知
//     *
//     * @param typeEnum 通知类型枚举
//     * @param data     数据
//     */
//    public void sendMessage(GuessNotityTypeEnum typeEnum, Object data) {
//        GuessNotityVO guessNotityVO = new GuessNotityVO();
//        guessNotityVO.setType(typeEnum.getType());
//        guessNotityVO.setMessage(typeEnum.getMessage());
//        guessNotityVO.setData(data);
//        String resultJson = JsonUtil.objectToJson(guessNotityVO);
//        assert resultJson != null;
//        guessWebSocketHandler.sendMessageToUsers(new TextMessage(resultJson));
//    }
//
//    /**
//     * 查询开始时间或结束时间是否在竞猜期内
//     *
//     * @param time 时间
//     * @return 数量
//     */
//    public Integer countByTime(String time) {
//        return guessMapper.countByTime(time);
//    }

}
