package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailMoney2;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法二投注金额Dao
 * @author Minced
 */
public interface GuessDetailMoney2Dao extends JpaRepository<GuessDetailMoney2, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法二投注金额
     */
    GuessDetailMoney2 findByGuessDetailId(Long guessDetailId);

}
