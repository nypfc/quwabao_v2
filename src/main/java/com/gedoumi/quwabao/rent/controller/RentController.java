package com.gedoumi.quwabao.rent.controller;

import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.rent.dataobj.model.Rent;
import com.gedoumi.quwabao.rent.dataobj.vo.RentVO;
import com.gedoumi.quwabao.rent.service.RentService;
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
@RequestMapping("/v2/rent")
@RestController
public class RentController {

    @Resource
    private RentService rentService;

    /**
     * 获取矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping("/list")
    public ResponseObject minerList() {
        List<Rent> rentList = rentService.getRentList();
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
