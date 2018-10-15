package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 竞猜详情Dao
 * @author Minced
 */
public interface GuessDetailDao extends JpaRepository<GuessDetail, Long> {

    /**
     * 根据ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 竞猜详情
     */
//    GuessDetail findById(Long guessDetailId);

    /**
     * 根据ID集合查询
     * @param idCollection ID集合
     * @return 竞猜详情
     */
    List<GuessDetail> findByIdIn(Collection<Long> idCollection);

    /**
     * 根据竞猜详情状态查询
     * @param guessDetailStatus 竞猜详情状态集合
     * @param pageable 分页
     * @return 竞猜详情集合
     */
    List<GuessDetail> findByGuessDetailStatusIn(List<Integer> guessDetailStatus, Pageable pageable);

    /**
     * 查询下注期的竞猜详情
     * @param guessDetailStatus 竞猜详情状态
     * @param pageable 分页
     * @return 竞猜详情集合
     */
    List<GuessDetail> findByGuessDetailStatus(Integer guessDetailStatus, Pageable pageable);

    /**
     * 查询当天时间的竞猜详情数量
     * @param date1 当前日期
     * @param date2 当前日期 + 1
     * @return 数量
     */
    Integer countByStartTimeBetween(Date date1, Date date2);

    /**
     * 查询已结束的竞猜详情列表
     * @param guessDetailStatus 竞猜详情状态
     * @param pageable 分页对象
     * @return 竞猜详情列表
     */
    List<GuessDetail> findByGuessDetailStatusOrderByStartTimeDesc(Integer guessDetailStatus, Pageable pageable);

}
