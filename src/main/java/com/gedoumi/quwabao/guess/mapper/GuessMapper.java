package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.Guess;
import org.apache.ibatis.annotations.Mapper;

/**
 * 竞猜期Mapper
 *
 * @author Minced
 */
@Mapper
public interface GuessMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 竞猜期对象
     */
    Guess selectById(Long id);

    /**
     * 查询最新的竞猜期
     *
     * @return 竞猜期对象
     */
    Guess selectLatest();

    /**
     * 添加
     *
     * @param guess 竞猜期对象
     * @return 数据库受影响行数
     */
    int insert(Guess guess);

    /**
     * 根据ID更新
     *
     * @param guess 竞猜期对象
     * @return 数据库受影响行数
     */
    int updateById(Guess guess);

}