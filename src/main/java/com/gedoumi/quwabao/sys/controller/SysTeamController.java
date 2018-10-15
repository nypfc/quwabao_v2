package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.asset.entity.UserTeam;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.asset.service.UserTeamService;
import com.gedoumi.quwabao.asset.vo.TeamRentVO;
import com.gedoumi.quwabao.asset.vo.UserTeamVO;
import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.util.JsonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/admin/userTeam")
public class SysTeamController {
	static Logger LOGGER = LoggerFactory.getLogger(SysTeamController.class);

	@Resource
	private UserAssetService userAssetService;

	@Resource
	private UserTeamService userTeamService;

	@RequestMapping(value = "/")
	public ModelAndView getUserTeam(){
		return new ModelAndView("team/userTeam");
	}

    @RequiresPermissions("team:read")
    @GetMapping("/page")
    public DataGrid getPage(PageParam param, UserTeamVO userTeamVO){

	    return userTeamService.getList(param, userTeamVO);
    }


    @RequestMapping("/{id}")
    public UserTeam toUpdate(@PathVariable("id")Long id){
        UserTeam userTeam = userTeamService.getById(id);
        return userTeam;
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
            UserTeam userTeam = userTeamService.getById(Long.parseLong(id));
            TeamRentVO teamRentVO = userAssetService.getTeamRentAsset(userTeam.getUser(),true);
            userTeam.setTotalCount(teamRentVO.getTotalCount());
            userTeam.setTotalRentAsset(teamRentVO.getTotalAsset());
            userTeamService.updateUserTeam(userTeam);
        }
        responseObject.setSuccess();
        return responseObject;
    }

    @RequestMapping("unFrozen/{ids}")
    public ResponseObject unFrozen(@PathVariable("ids")String[] ids){
        ResponseObject responseObject = new ResponseObject();
        for (String id : ids) {
            UserTeam userTeam = userTeamService.getById(Long.parseLong(id));
            try {
                userAssetService.unFrozen(userTeam);
            } catch (BusinessException e) {
                LOGGER.error(e.getCodeEnum().getMessage(), e);
                responseObject.setInfo(e.getCodeEnum());
                return responseObject;
            }
        }
        responseObject.setSuccess();
        return responseObject;
    }

    @RequestMapping("applyReward/{ids}")
    public ResponseObject applyReward(@PathVariable("ids")String[] ids){
        ResponseObject responseObject = new ResponseObject();
        for (String id : ids) {
            UserTeam userTeam = userTeamService.getById(Long.parseLong(id));
            try {
                userAssetService.applyReward(userTeam);
            } catch (BusinessException e) {
                LOGGER.error(e.getCodeEnum().getMessage(), e);
                responseObject.setInfo(e.getCodeEnum());
                return responseObject;
            }
        }
        responseObject.setSuccess();
        return responseObject;
    }

    @RequestMapping("/update")
    public ResponseObject update(UserTeam userTeam) {
        LOGGER.info("in update userTeam = {}", JsonUtil.objectToJson(userTeam));
        Date now = new Date();
        ResponseObject responseObject = new ResponseObject();
        //update
        if(userTeam.getId() != null){
            UserTeam orgTeam = userTeamService.getById(userTeam.getId());
            orgTeam.setBaseAsset(userTeam.getBaseAsset());
            orgTeam.setUnFrozenAsset(userTeam.getUnFrozenAsset());
            orgTeam.setUpdateTime(now);
            userTeamService.updateUserTeam(orgTeam, userTeam.getUser().getUserType());
            responseObject.setSuccess();
            responseObject.setData(orgTeam);
            return responseObject;
        }else {
            //add
            try {
                userAssetService.addUserTeam(userTeam);
            } catch (BusinessException e) {
                LOGGER.error(e.getCodeEnum().getMessage(), e);
                responseObject.setInfo(e.getCodeEnum());
                return responseObject;
            }
            responseObject.setSuccess();
            responseObject.setData(userTeam);
            return responseObject;
        }

    }



}
