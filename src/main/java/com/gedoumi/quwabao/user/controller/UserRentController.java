package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.RentForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.dataobj.vo.UserRentVO;
import com.gedoumi.quwabao.user.service.UserRentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户矿机Controller
 *
 * @author Minced
 */
@RequestMapping("/v2/user")
@RestController
public class UserRentController {

    @Resource
    private UserRentService userRentService;

    /**
     * 获取用户已租用矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping("/rent")
    public ResponseObject userRents() {
        // 获取用户信息
        User user = ContextUtil.getUserFromRequest();
        // 获取用户租用矿机信息
        List<UserRent> userRents = userRentService.getUserRents(user.getId());
        // 遍历集合封装返回数据
        List<UserRentVO> userRentVOList = userRents.stream().map(userRent -> {
            UserRentVO userRentVO = new UserRentVO();
            userRentVO.setRentName(userRent.getRent().getName());
            userRentVO.setLastDig(userRent.getLastDig().stripTrailingZeros().toPlainString());
            // 矿机剩余收益
            BigDecimal remainProfit = userRent.getTotalAsset().subtract(userRent.getAlreadyDig())
                    .setScale(5, BigDecimal.ROUND_DOWN).stripTrailingZeros();
            userRentVO.setRemainProfit(remainProfit.toPlainString());
            return userRentVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(userRentVOList);
    }

    /**
     * 矿机租用
     *
     * @param rentForm 租用表单
     * @return ResponseObject
     */
    @PostMapping("/rent")
    public ResponseObject rent(@RequestBody @Valid RentForm rentForm) {
        userRentService.rent(rentForm);
        return new ResponseObject();
    }

}
