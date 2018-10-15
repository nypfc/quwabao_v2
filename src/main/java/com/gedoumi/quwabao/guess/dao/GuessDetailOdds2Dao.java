package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailOdds2;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法二Dao
 * @author Minced
 */
public interface GuessDetailOdds2Dao extends JpaRepository<GuessDetailOdds2, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法二赔率对象
     */
    GuessDetailOdds2 findByGuessDetailId(Long guessDetailId);

}
