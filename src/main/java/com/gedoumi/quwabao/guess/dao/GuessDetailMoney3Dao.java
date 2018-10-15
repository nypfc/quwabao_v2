package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailMoney3;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法三投注金额Dao
 * @author Minced
 */
public interface GuessDetailMoney3Dao extends JpaRepository<GuessDetailMoney3, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法三投注金额
     */
    GuessDetailMoney3 findByGuessDetailId(Long guessDetailId);

}
