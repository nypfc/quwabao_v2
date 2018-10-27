package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.UserTeamVO;
import com.gedoumi.quwabao.user.service.UserTeamService;
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
@RequestMapping("/v2/team")
@RestController
public class UserTeamController {

    @Resource
    private UserTeamService userTeamService;

    @GetMapping("/list")
    public ResponseObject getUserTeamList() {
        User user = ContextUtil.getUserFromRequest();
        List<User> users = userTeamService.getChildUser(user.getId());
        List<UserTeamVO> userTeamVOList = users.stream().map(u -> {
            UserTeamVO userTeamVO = new UserTeamVO();
            userTeamVO.setMobile(u.getMobilePhone());
            userTeamVO.setUsername(u.getUsername());
            return userTeamVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(userTeamVOList);
    }

}
