package com.gedoumi.quwabao.api.gateway;

import com.gedoumi.quwabao.api.gateway.vo.QueryVO;
import com.gedoumi.quwabao.api.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.asset.entity.UserAssetDetail;
import com.gedoumi.quwabao.asset.service.AssetDetailService;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.common.annotation.PfcLogAspect;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.user.service.UserService;
import com.gedoumi.quwabao.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/pfc")
public class PfcApiController {
	 Logger logger = LoggerFactory.getLogger(PfcApiController.class);

	@Resource
	private UserAssetService userAssetService;

	@Resource
	private AssetDetailService assetDetailService;

	@Resource
	private UserService userService;

	@PfcLogAspect
	@PostMapping(value = "/recharge")
	ApiResponse recharge(RechargeVO rechargeVO){
		logger.info("recharge begin {}", JsonUtil.objectToJson(rechargeVO));

		ApiResponse apiResponse = new ApiResponse();
		if(rechargeVO == null || rechargeVO.getTs() == null
				|| StringUtils.isEmpty(rechargeVO.getSeq())
				|| StringUtils.isEmpty(rechargeVO.getPfc_account())
				|| StringUtils.isEmpty(rechargeVO.getAsset_name())
				|| StringUtils.isEmpty(rechargeVO.getAmount())
				|| StringUtils.isEmpty(rechargeVO.getSig())){
			apiResponse.setAccessError();
			return apiResponse;
		}
		//时间戳校验，20s内可以访问
//		long currentTimeMillis = System.currentTimeMillis();
//		//ts second
//		long ts = rechargeVO.getTs();
//		int counts = (int)(currentTimeMillis/ 1000 - ts);
//		if(counts > 20 || counts <0){
//			logger.info("ts not validate ");
//			apiResponse.setAccessError();
//			return apiResponse;
//		}

		String sign = rechargeVO.generateSign(TransApi.path);

		if(!StringUtils.equals(sign,rechargeVO.getSig())){
			apiResponse.setAccessError();
			return apiResponse;
		}
		User user = userService.getByMobilePhone(rechargeVO.getPfc_account());
		if(user == null){
			logger.info("{}用户不存在" , rechargeVO.getPfc_account());
//			logger.info(" begin add user ");
//			user = new User();
//			user.setMobilePhone(rechargeVO.getPfc_account());
//			String salt = new Md5Hash(user.getMobilePhone()).toString();
//			user.setPassword(new Md5Hash(User.PWD_INIT, salt).toString());
//			user.setUserStatus(UserStatus.Enable.getValue());
//			Date now = new Date();
//			user.setUpdateTime(now);
//			user.setRegisterTime(now);
//			user.setLastLoginTime(now);
//			user.setUserType(UserType.Level_0.getValue());
//			userService.addUser(user);
			apiResponse.setAccountError();
			return apiResponse;
		}

		UserAssetDetail assetDetail = assetDetailService.getByApiTransSeq(rechargeVO.getSeq());
		if(assetDetail == null){

			UserAssetDetail detail = new UserAssetDetail();
			detail.setUser(user);
			detail.setMoney(new BigDecimal(rechargeVO.getAmount()));
			detail.setProfit(BigDecimal.ZERO);
			detail.setProfitExt(BigDecimal.ZERO);
			detail.setTransType(TransType.NetIn.getValue());
			detail.setApiTransSeq(rechargeVO.getSeq());
			userAssetService.addAsset(detail);

		}
		QueryVO queryVO = new QueryVO();
		queryVO.setPfc_account(user.getMobilePhone());
		queryVO.setEth_address(user.getEthAddress());
		apiResponse.setData(queryVO);
		apiResponse.setSuccess();
		logger.info("recharge end ");
		return apiResponse;
	}



}
