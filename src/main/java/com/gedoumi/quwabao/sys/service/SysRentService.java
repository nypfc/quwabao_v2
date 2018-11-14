package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.enums.RentStatus;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.mapper.SysRentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 矿机Service
 *
 * @author Minced
 */
@Service
public class SysRentService {

    @Resource
    private SysRentMapper sysRentMapper;

    /**
     * 获取矿机列表
     *
     * @return 矿机集合
     */
    public List<SysRent> getRents() {
        return sysRentMapper.selectActiveRents(RentStatus.ACTIVE.getValue());
    }

    /**
     * 获取指定类型的矿机列表
     *
     * @param types 类型集合
     * @return 矿机集合
     */
    public List<SysRent> getRentsInType(Collection<Integer> types) {
        return sysRentMapper.selectInTypes(types);
    }

    /**
     * 获取矿机
     *
     * @param rentType 矿机类型
     * @return 矿机对象
     */
    public SysRent getRent(Integer rentType) {
        return sysRentMapper.selectByType(rentType);
    }

}
