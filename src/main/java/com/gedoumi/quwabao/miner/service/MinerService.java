package com.gedoumi.quwabao.miner.service;

import com.gedoumi.quwabao.common.enums.RentStatus;
import com.gedoumi.quwabao.miner.dataobj.model.Rent;
import com.gedoumi.quwabao.miner.mapper.MinerMapper;
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
public class MinerService {

    @Resource
    private MinerMapper minerMapper;

    /**
     * 获取矿机列表
     *
     * @return 矿机集合
     */
    public List<Rent> getRentList() {
        return minerMapper.queryList(RentStatus.ACTIVE.getValue());
    }

    /**
     * 获取矿机
     *
     * @param rentType 矿机类型
     * @return 矿机对象
     */
    public Rent getRent(Integer rentType) {
        return minerMapper.queryRent(rentType);
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
