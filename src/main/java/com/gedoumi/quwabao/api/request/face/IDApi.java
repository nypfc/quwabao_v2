package com.gedoumi.quwabao.api.request.face;

import com.gedoumi.quwabao.api.request.face.vo.FaceVO;
import com.gedoumi.quwabao.common.utils.HttpClientUtils;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IDApi {

    private static String url = "http://121.41.42.121:8080";
    private static String path = "/v2/id-server";

    private static String getUrl() {
        String value = PropertiesUtils.getInstance().getValue("idapi.url");
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        return url;
    }

    private static String getFormString(HttpPost httpPost, HttpEntity formEntity) throws IOException {
        log.info("formEntity = {}", formEntity);
        httpPost.setEntity(formEntity);
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("call result = {}", result);
        return result;
    }

    public static IDApiResponse testID(FaceVO faceVO) throws Exception {
        log.info("faceVO {}", faceVO);
        HttpPost httpPost = new HttpPost(getUrl() + path);
        String result = getFormString(httpPost, getFormEntity(faceVO));
        log.info("result = {}", result);
        return JsonUtil.jsonToPojo(result, IDApiResponse.class);
    }

    private static HttpEntity getFormEntity(FaceVO faceVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("mall_id", faceVO.getMall_id()));
        formParams.add(new BasicNameValuePair("realname", faceVO.getRealname()));
        formParams.add(new BasicNameValuePair("idcard", faceVO.getIdcard()));
        formParams.add(new BasicNameValuePair("tm", faceVO.getTm()));
        formParams.add(new BasicNameValuePair("sign", faceVO.getSign()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

}
