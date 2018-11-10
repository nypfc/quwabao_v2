package com.pfc.quwabao.miner.controller;

import com.pfc.quwabao.common.utils.ResponseObject;
import com.pfc.quwabao.miner.dataobj.model.Rent;
import com.pfc.quwabao.miner.dataobj.vo.RentVO;
import com.pfc.quwabao.miner.service.MinerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 矿机Controller
 *
 * @author Minced
 */
@RequestMapping("/v2/miner")
@RestController
public class MinerController {

    @Resource
    private MinerService minerService;

    /**
     * 获取矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping("/list")
    public ResponseObject minerList() {
        List<Rent> rentList = minerService.getRentList();
        // 封装返回数据
        List<RentVO> rentVOList = rentList.stream().map(rent -> {
            RentVO rentVO = new RentVO();
            rentVO.setRentName(rent.getName());
            rentVO.setRentMoney(rent.getMoney().stripTrailingZeros().toPlainString());
            rentVO.setTotalRemain(rent.getProfitMoneyExt().stripTrailingZeros().toPlainString());
            return rentVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(rentVOList);
    }

}
