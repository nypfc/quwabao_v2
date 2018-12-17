package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailMoney1;

public interface GuessDetailMoney1Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(GuessDetailMoney1 record);

    int insertSelective(GuessDetailMoney1 record);

    GuessDetailMoney1 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuessDetailMoney1 record);

    int updateByPrimaryKey(GuessDetailMoney1 record);
}