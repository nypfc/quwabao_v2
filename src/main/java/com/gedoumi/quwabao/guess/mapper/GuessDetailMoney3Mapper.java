package com.gedoumi.quwabao.guess.mapper;

import com.gedoumi.quwabao.guess.dataobj.model.GuessDetailMoney3;

public interface GuessDetailMoney3Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(GuessDetailMoney3 record);

    int insertSelective(GuessDetailMoney3 record);

    GuessDetailMoney3 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuessDetailMoney3 record);

    int updateByPrimaryKey(GuessDetailMoney3 record);
}