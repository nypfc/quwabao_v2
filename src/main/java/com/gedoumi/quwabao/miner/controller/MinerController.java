package com.gedoumi.quwabao.miner.controller;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.miner.dataobj.model.Rent;
import com.gedoumi.quwabao.miner.dataobj.model.UserRent;
import com.gedoumi.quwabao.miner.dataobj.vo.RentVO;
import com.gedoumi.quwabao.miner.dataobj.vo.UserRentInfoVO;
import com.gedoumi.quwabao.miner.dataobj.vo.UserRentVO;
import com.gedoumi.quwabao.miner.service.MinerService;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
            rentVO.setRentMoney(String.valueOf(rent.getMoney()));
            rentVO.setTotalRemain(String.valueOf(rent.getProfitMoneyExt()));
            return rentVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(rentVOList);
    }

    /**
     * 获取用户已租用矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping("/rentList")
    public ResponseObject userRentList() {
        // 获取用户信息
        User user = ContextUtil.getUserFromRequest();
        // 获取用户租用矿机信息
        List<UserRent> userRents = minerService.getUserRent(user.getId());
        // 遍历集合封装返回数据
        List<UserRentVO> userRentVOList = userRents.stream().map(userRent -> {
            UserRentVO userRentVO = new UserRentVO();
            userRentVO.setRentName(userRent.getRent().getName());
            userRentVO.setRentMoney(String.valueOf(userRent.getRent().getMoney()));
            userRentVO.setLastDig(String.valueOf(userRent.getLastDig()));
            // 矿机剩余收益
            BigDecimal remainProfit = userRent.getTotalAsset().subtract(userRent.getAlreadyDig()).setScale(5, BigDecimal.ROUND_DOWN);
            userRentVO.setRemainProfit(String.valueOf(remainProfit));
            return userRentVO;
        }).collect(Collectors.toList());
        UserRentInfoVO userRentInfoVO = new UserRentInfoVO();
        userRentInfoVO.setMobile(user.getMobilePhone());
        userRentInfoVO.setUserRentVOList(userRentVOList);
        return new ResponseObject<>(userRentInfoVO);
    }

}
