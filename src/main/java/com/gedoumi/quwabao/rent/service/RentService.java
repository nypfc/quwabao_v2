package com.gedoumi.quwabao.rent.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gedoumi.quwabao.common.enums.RentStatus;
import com.gedoumi.quwabao.rent.dataobj.model.Rent;
import com.gedoumi.quwabao.rent.mapper.RentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 矿机Service
 *
 * @author Minced
 */
@Service
public class RentService {

    @Resource
    private RentMapper rentMapper;

    /**
     * 获取矿机列表
     *
     * @return 矿机集合
     */
    public List<Rent> getRentList() {
        return rentMapper.selectList(new LambdaQueryWrapper<Rent>().eq(Rent::getRentStatus, RentStatus.ACTIVE.getValue()));
    }

    /**
     * 获取矿机
     *
     * @param rentType 矿机类型
     * @return 矿机对象
     */
    public Rent getRent(Integer rentType) {
        return rentMapper.selectOne(new LambdaQueryWrapper<Rent>().eq(Rent::getRentCode, rentType));
    }

    /**
     * 计算挖矿收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void digJob() {

    }

    /**
     * 计算推荐人收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask() {

    }

}
