package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserAssetDetailMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserAssetDetailMapper userAssetDetailMapper;

    @Test
    public void test() {
        List<Integer> transTypes = Lists.newArrayList();
        transTypes.add(TransType.FrozenIn.getValue());
        transTypes.add(TransType.FrozenOut.getValue());
        transTypes.add(TransType.TeamInit.getValue());
        transTypes.add(TransType.TransIn.getValue());
        transTypes.add(TransType.TransOut.getValue());
        transTypes.add(TransType.Rent.getValue());
        transTypes.add(TransType.NetIn.getValue());
        transTypes.add(TransType.NetOut.getValue());
        List<UserAssetDetail> userAssetDetails = userAssetDetailMapper.selectBatchIds(transTypes);
        System.out.println(JsonUtil.objectToJson(userAssetDetails));
    }

}