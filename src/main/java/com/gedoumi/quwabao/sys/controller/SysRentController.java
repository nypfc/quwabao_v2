package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.dataobj.vo.SysRentVO;
import com.gedoumi.quwabao.sys.service.SysRentService;
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
public class SysRentController {

    @Resource
    private SysRentService sysRentService;

    /**
     * 获取矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping
    public ResponseObject rents() {
        List<SysRent> rentList = sysRentService.getRents();
        // 封装返回数据
        List<SysRentVO> rentVOList = rentList.stream().map(rent -> {
            SysRentVO rentVO = new SysRentVO();
            rentVO.setRentName(rent.getName());
            rentVO.setRentMoney(rent.getMoney().stripTrailingZeros().toPlainString());
            rentVO.setTotalRemain(rent.getProfitMoneyExt().stripTrailingZeros().toPlainString());
            return rentVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(rentVOList);
    }

}
