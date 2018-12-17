package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailMoney2;

public interface GuessDetailMoney2Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(GuessDetailMoney2 record);

    int insertSelective(GuessDetailMoney2 record);

    GuessDetailMoney2 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuessDetailMoney2 record);

    int updateByPrimaryKey(GuessDetailMoney2 record);
}