package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailOdds1;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法一Dao
 * @author Minced
 */
public interface GuessDetailOdds1Dao extends JpaRepository<GuessDetailOdds1, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法一赔率对象
     */
    GuessDetailOdds1 findByGuessDetailId(Long guessDetailId);

}
