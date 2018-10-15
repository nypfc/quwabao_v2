package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.asset.vo.UserInfoVO;
import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
import com.gedoumi.quwabao.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/user")
public class ManageUserController {
	static Logger LOGGER = LoggerFactory.getLogger(ManageUserController.class);

	@Resource
	private UserService userService;
	@RequestMapping(value = "/")
	public ModelAndView getUserTeam(){
		return new ModelAndView("user/user");
	}

    @RequiresPermissions("user:read")
    @GetMapping("/page")
    public DataGrid getPage(PageParam param, UserInfoVO userInfoVO){

	    return userService.getPageList(param, userInfoVO);
    }


    /**
     * 同步矿机信息
     * @param ids
     * @return
     */
    @RequestMapping("cal/{ids}")
    public ResponseObject cal(@PathVariable("ids")String[] ids){
        ResponseObject responseObject = new ResponseObject();
        for (String id : ids) {
            User user = userService.getById(Long.parseLong(id));
            user.setValidateStatus(UserValidateStatus.Pass.getValue());
            userService.update(user);
        }
        responseObject.setSuccess();
        return responseObject;
    }




}
