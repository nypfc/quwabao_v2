package com.gedoumi.quwabao.miner.service;

import com.gedoumi.quwabao.common.enums.RentStatus;
import com.gedoumi.quwabao.common.enums.UserRentStatus;
import com.gedoumi.quwabao.miner.dataobj.model.Rent;
import com.gedoumi.quwabao.miner.dataobj.model.UserRent;
import com.gedoumi.quwabao.miner.mapper.MinerMapper;
import org.springframework.stereotype.Service;

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
     * 获取用户租用矿机信息列表
     *
     * @param userId 用户ID
     * @return 矿机信息集合
     */
    public List<UserRent> getUserRent(Long userId) {
        return minerMapper.queryUserRent(userId, UserRentStatus.Active.getValue());
    }

}
