package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.Guess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * 竞猜期Dao
 * @author Minced
 */
public interface GuessDao extends JpaRepository<Guess, Long> {

    /**
     * 根据竞猜期ID查询竞猜期
     * @param guessId 竞猜期ID
     * @return 竞猜期
     */
//    Guess findById(Long guessId);

    /**
     * 根据竞猜期状态查询
     * @param guessStatus 竞猜期状态
     * @param pageable 分页
     * @return 竞猜期
     */
    List<Guess> findByGuessStatus(Integer guessStatus, Pageable pageable);

    /**
     * 根据开始时间查询
     * @param startDateTime 开始时间
     * @return 竞猜期
     */
    Guess findByStartTime(Date startDateTime);

}
