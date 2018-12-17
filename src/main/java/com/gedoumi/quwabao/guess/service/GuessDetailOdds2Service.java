package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds2;
import com.gedoumi.quwabao.guess.mapper.GuessDetailOdds2Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 玩法2赔率Service
 *
 * @author Minced
 */
@Service
public class GuessDetailOdds2Service {

    @Resource
    private GuessDetailOdds2Mapper guessDetailOdds2Mapper;

    /**
     * 获取玩法2赔率
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法1赔率
     */
    public GuessDetailOdds2 getGuessDetailOdds2(Long guessDetailId) {
        return guessDetailOdds2Mapper.selectByGuessDetailId(guessDetailId);
    }

}
