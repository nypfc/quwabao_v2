package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds3;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩法3赔率
 *
 * @author Minced
 */
@Mapper
public interface GuessDetailOdds3Mapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 玩法3赔率
     */
    GuessDetailOdds3 selectById(Long id);

    /**
     * 根据竞猜详情ID查询
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法3赔率
     */
    GuessDetailOdds3 selectByGuessDetailId(Long guessDetailId);

    /**
     * 添加
     *
     * @param guessDetailOdds3 玩法3赔率
     * @return 数据库受影响行数
     */
    int insert(GuessDetailOdds3 guessDetailOdds3);

    /**
     * 根据ID更新
     *
     * @param guessDetailOdds3 玩法3赔率
     * @return 数据库受影响行数
     */
    int updateById(GuessDetailOdds3 guessDetailOdds3);

}