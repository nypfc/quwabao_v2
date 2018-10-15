package com.gedoumi.quwabao.asset.controller;

import com.gedoumi.quwabao.asset.entity.UserAssetDetail;
import com.gedoumi.quwabao.asset.service.AssetDetailService;
import com.gedoumi.quwabao.asset.vo.DetailTypeVO;
import com.gedoumi.quwabao.asset.vo.UserAssetDetailVO;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.utils.SessionUtil;
import com.gedoumi.quwabao.user.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/v1/detail")
public class AssetDetailController {

    @Resource
    private AssetDetailService assetDetailService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/page")
    ResponseObject getAssetDetail(@RequestBody DetailTypeVO detailTypeVO) {
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        detailTypeVO.setUserId(user.getId());
        PageParam pageParam = new PageParam();
        pageParam.setPage(detailTypeVO.getPage());
        pageParam.setRows(detailTypeVO.getRows());
        List<UserAssetDetail> data = assetDetailService.getList(pageParam, detailTypeVO);
        ResponseObject responseObject = new ResponseObject();
        List<UserAssetDetailVO> dataVO = Lists.newArrayList();
        for (UserAssetDetail assetDetail : data) {
            UserAssetDetailVO vo = new UserAssetDetailVO();
            BeanUtils.copyProperties(assetDetail, vo);
            dataVO.add(vo);
        }
        responseObject.setData(data);
        responseObject.setSuccess();
        return responseObject;
    }

}
