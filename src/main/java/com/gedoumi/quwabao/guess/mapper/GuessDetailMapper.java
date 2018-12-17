package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 竞猜详情（竞猜周期）Mapper
 *
 * @author Minced
 */
@Mapper
public interface GuessDetailMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 竞猜详情对象
     */
    GuessDetail selectById(Long id);

    /**
     * 获取最新的竞猜详情
     *
     * @return 竞猜详情对象
     */
    GuessDetail selectLatest();

    /**
     * 根据ID集合查询
     *
     * @param collection ID集合
     * @return 竞猜详情集合
     */
    List<GuessDetail> selectByIdIn(Collection<Long> collection);

    /**
     * 根据状态查询竞猜详情
     *
     * @param guessDetailStatus 竞猜详情状态
     * @return 竞猜详情集合
     */
    List<GuessDetail> selectByStatus(Integer guessDetailStatus);

    /**
     * 添加
     *
     * @param guessDetail 竞猜详情
     * @return 数据库受影响行数
     */
    int insert(GuessDetail guessDetail);

    /**
     * 根据ID更新
     *
     * @param guessDetail 竞猜详情
     * @return 数据库受影响行数
     */
    int updateById(GuessDetail guessDetail);

}