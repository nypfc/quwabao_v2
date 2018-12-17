package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessBet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 竞猜下注Mapper
 *
 * @author Minced
 */
@Mapper
public interface GuessBetMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 竞猜下注
     */
    GuessBet selectById(Long id);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 竞猜下注集合
     */
    List<GuessBet> selectByUserId(Long userId);

    /**
     * 添加
     *
     * @param guessBet 竞猜下注
     * @return 数据库受影响行数
     */
    int insert(GuessBet guessBet);

    /**
     * 根据ID更新
     *
     * @param guessBet 竞猜下注
     * @return 数据库受影响行数
     */
    int updateById(GuessBet guessBet);

}