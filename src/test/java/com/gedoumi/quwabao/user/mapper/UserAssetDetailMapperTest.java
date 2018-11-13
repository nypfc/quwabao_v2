package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
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
        transTypes.add(TransTypeEnum.FrozenIn.getValue());
        transTypes.add(TransTypeEnum.FrozenOut.getValue());
        transTypes.add(TransTypeEnum.TeamInit.getValue());
        transTypes.add(TransTypeEnum.TransIn.getValue());
        transTypes.add(TransTypeEnum.TransOut.getValue());
        transTypes.add(TransTypeEnum.Rent.getValue());
        transTypes.add(TransTypeEnum.NetIn.getValue());
        transTypes.add(TransTypeEnum.NetOut.getValue());
        List<UserAssetDetail> userAssetDetails = userAssetDetailMapper.selectBatchIds(transTypes);
        System.out.println(JsonUtil.objectToJson(userAssetDetails));
    }

}