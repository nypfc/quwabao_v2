package com.gedoumi.quwabao.guess.service;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds3;
import com.gedoumi.quwabao.guess.mapper.GuessDetailOdds3Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 玩法3赔率Service
 *
 * @author Minced
 */
@Service
public class GuessDetailOdds3Service {

    @Resource
    private GuessDetailOdds3Mapper guessDetailOdds3Mapper;

    /**
     * 获取玩法1赔率
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法1赔率
     */
    public GuessDetailOdds3 getGuessDetailOdds3(Long guessDetailId) {
        return guessDetailOdds3Mapper.selectByGuessDetailId(guessDetailId);
    }

}
