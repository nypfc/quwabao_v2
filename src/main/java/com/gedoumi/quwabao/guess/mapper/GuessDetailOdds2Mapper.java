package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailOdds2;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩法2赔率Mapper
 *
 * @author Minced
 */
@Mapper
public interface GuessDetailOdds2Mapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 玩法2赔率
     */
    GuessDetailOdds2 selectByPrimaryKey(Long id);

    /**
     * 根据竞猜详情ID查询
     *
     * @param guessDetailId 竞猜详情ID
     * @return 玩法2赔率
     */
    GuessDetailOdds2 selectByGuessDetailId(Long guessDetailId);

    /**
     * 添加
     *
     * @param guessDetailOdds2 玩法2赔率
     * @return 数据库受影响行数
     */
    int insert(GuessDetailOdds2 guessDetailOdds2);

    /**
     * 根据ID更新
     *
     * @param guessDetailOdds2 玩法2赔率
     * @return 数据库受影响行数
     */
    int updateById(GuessDetailOdds2 guessDetailOdds2);

}