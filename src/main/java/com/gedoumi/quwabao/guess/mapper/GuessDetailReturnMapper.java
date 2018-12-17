package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailReturn;

public interface GuessDetailReturnMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GuessDetailReturn record);

    int insertSelective(GuessDetailReturn record);

    GuessDetailReturn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuessDetailReturn record);

    int updateByPrimaryKey(GuessDetailReturn record);
}