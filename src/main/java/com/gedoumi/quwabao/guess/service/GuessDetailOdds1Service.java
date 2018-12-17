package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds1;
import com.gedoumi.quwabao.guess.mapper.GuessDetailOdds1Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 玩法1赔率Service
 *
 * @author Minced
 */
@Service
public class GuessDetailOdds1Service {

    @Resource
    private GuessDetailOdds1Mapper guessDetailOdds1Mapper;

    /**
     * 获取玩法1赔率
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法1赔率
     */
    public GuessDetailOdds1 getGuessDetailOdds1(Long guessDetailId) {
        return guessDetailOdds1Mapper.selectByGuessDetailId(guessDetailId);
    }

}
