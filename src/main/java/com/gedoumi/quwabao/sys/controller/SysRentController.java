package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.asset.entity.UserRent;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.asset.service.UserRentService;
import com.gedoumi.quwabao.asset.vo.SysRentVO;
import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.sys.entity.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/rent")
public class SysRentController {
	static Logger LOGGER = LoggerFactory.getLogger(SysRentController.class);

	@Resource
	private UserAssetService userAssetService;

	@Resource
	private SysRentService rentService;

	@Resource
	private UserRentService userRentService;

	@Resource
	private AppConfig appConfig;

	@RequestMapping(value = "/")
	public ModelAndView getUserTeam(){
		return new ModelAndView("rent/rent");
	}

    @RequiresPermissions("rent:read")
    @GetMapping("/page")
    public DataGrid getPage(PageParam param, SysRentVO sysRentVO){

	    return rentService.getList(param, sysRentVO);
    }


    @RequestMapping("/{id}")
    public SysRent toUpdate(@PathVariable("id")Long id){
        SysRent sysRent = rentService.getById(id);
        LOGGER.info("in update sysRent = {}", JsonUtil.objectToJson(sysRent));
        return sysRent;
    }



    @RequestMapping("/update")
    public ResponseObject update(SysRent sysRent) {
        LOGGER.info("in update sysRent = {}", JsonUtil.objectToJson(sysRent));
        ResponseObject responseObject = new ResponseObject();
        //update
        BigDecimal rate = sysRent.getRate();
        rate = rate.divide(new BigDecimal(100), Constants.SCALE, BigDecimal.ROUND_HALF_UP);
        sysRent.setRate(rate);
        if(sysRent.getId() != null){
            SysRent orgRent = rentService.getById(sysRent.getId());
            orgRent.setName(sysRent.getName());
            orgRent.setMaxNumber(sysRent.getMaxNumber());
            orgRent.setRentStatus(sysRent.getRentStatus());
            orgRent.setRate(rate);
            orgRent.setMoney(sysRent.getMoney());
            orgRent.setDigMax(sysRent.getDigMax());
            orgRent.setDigMin(sysRent.getDigMin());
            orgRent.setCode(sysRent.getCode());
            rentService.updateInfo(orgRent);
            responseObject.setSuccess();
            responseObject.setData(orgRent);
            return responseObject;
        }else {
            //add
            rentService.add(sysRent);
            responseObject.setSuccess();
            responseObject.setData(sysRent);
            return responseObject;
        }

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
            SysRent sysRent = rentService.getById(Long.parseLong(id));
            List<UserRent> userRents = userRentService.getByRentType(Integer.parseInt(sysRent.getCode()));
            if(CollectionUtils.isEmpty(userRents)){
                sysRent.setRentNumber(0);
            }else {
                sysRent.setRentNumber(userRents.size());
            }

            rentService.updateRentNumber(sysRent);
        }
        responseObject.setSuccess();
        return responseObject;
    }

    @RequestMapping("onTrans")
    public ResponseObject onTrans(){
        ResponseObject responseObject = new ResponseObject();
        appConfig.setOnTrans(!appConfig.isOnTrans());
        responseObject.setSuccess();
        responseObject.setData(appConfig.isOnTrans());
        if(appConfig.isOnTrans()){
            userAssetService.calInitBaseAsset();
        }

        return responseObject;
    }

    @RequestMapping("getOnTrans")
    public ResponseObject getOnTrans(){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        responseObject.setData(appConfig.isOnTrans());
        return responseObject;
    }



}
