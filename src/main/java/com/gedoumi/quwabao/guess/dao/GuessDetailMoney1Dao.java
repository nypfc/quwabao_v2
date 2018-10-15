package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailMoney1;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法一投注金额Dao
 * @author Minced
 */
public interface GuessDetailMoney1Dao extends JpaRepository<GuessDetailMoney1, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法一投注金额
     */
    GuessDetailMoney1 findByGuessDetailId(Long guessDetailId);

}
