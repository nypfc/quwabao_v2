package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds1;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩法1赔率Mapper
 *
 * @author Minced
 */
@Mapper
public interface GuessDetailOdds1Mapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 玩法1赔率
     */
    GuessDetailOdds1 selectById(Long id);

    /**
     * 根据竞猜详情ID查询
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法1赔率
     */
    GuessDetailOdds1 selectByGuessDetailId(Long guessDetailId);

    /**
     * 添加
     *
     * @param guessDetailOdds1 玩法1赔率
     * @return 数据库受影响行数
     */
    int insert(GuessDetailOdds1 guessDetailOdds1);

    /**
     * 根据ID更新
     *
     * @param guessDetailOdds1 玩法1赔率
     * @return 数据库受影响行数
     */
    int updateById(GuessDetailOdds1 guessDetailOdds1);

}