package com.gedoumi.quwabao.guess.service;

import com.gedoumi.guess.websocket.GuessWebSocketHandler;
import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.guess.dataobj.model.Guess;
import com.gedoumi.quwabao.guess.dataobj.vo.GuessNotityVO;
import com.gedoumi.quwabao.guess.mapper.GuessMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 竞猜时间段Service
 *
 * @author Minced
 */
@Service
public class GuessService {

    @Resource
    private GuessMapper guessMapper;

    @Resource
    private GuessWebSocketHandler guessWebSocketHandler;

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

    /**
     * 根据竞猜期开始时间查询竞猜期
     *
     * @param startDateTime 开始时间
     * @return 竞猜期对象
     */
    public Guess findByStartTime(Date startDateTime) {
        return guessMapper.findByStartTime(startDateTime);
    }

    /**
     * 向前端发送WebSocket通知
     *
     * @param typeEnum 通知类型枚举
     * @param data     数据
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

    /**
     * 查询开始时间或结束时间是否在竞猜期内
     *
     * @param time 时间
     * @return 数量
     */
    public Integer countByTime(String time) {
        return guessMapper.countByTime(time);
    }

}
