package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentNumberDTO;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.UserTeamVO;
import com.gedoumi.quwabao.user.service.UserRentService;
import com.gedoumi.quwabao.user.service.UserTeamService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户团队Controller
 *
 * @author Minced
 */
@RequestMapping("/v2/user/team")
@RestController
public class UserTeamController {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserRentService userRentService;

    /**
     * 获取用户团队信息
     *
     * @return ResponseObject
     */
    @GetMapping("/list")
    public ResponseObject getUserTeamList() {
        User user = ContextUtil.getUserFromRequest();
        // 获取当前用户的下级用户
        List<User> users = userTeamService.getChildUser(user.getId());
        // 没有用户直接返回空集合
        if (CollectionUtils.isEmpty(users))
            return new ResponseObject<>(users);
        // 获取下级用户的ID集合并获取矿机数量
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<UserRentNumberDTO> numbers = userRentService.getUserRentNumber(userIds);
        // 封装返回信息
        List<UserTeamVO> userTeamVOList = users.stream().map(u -> {
            UserTeamVO userTeamVO = new UserTeamVO();
            userTeamVO.setMobile(u.getMobilePhone());
            userTeamVO.setUsername(u.getUsername());
            // 遍历矿机数量集合，如果ID相同，存入矿机数量
            numbers.forEach(number -> {
                if (number.getUserId().equals(u.getId()))
                    userTeamVO.setRentNumber(number.getNumber());
            });
            return userTeamVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(userTeamVOList);
    }

}
