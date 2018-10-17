package com.gedoumi.quwabao.common.config;

import com.gedoumi.quwabao.common.enums.VersionType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AppConfig {


    private AppConfig() {
    }

    private  Map<String,Object> configContext = new HashMap<>();

    public Map<String, Object> getConfigContext() {
        return configContext;
    }

    public void setConfigContext(Map<String, Object> configContext) {
        this.configContext = configContext;
    }

    public  synchronized AtomicInteger getUserLock(String mobile) {
        if (StringUtils.isEmpty(mobile))
            return null;
        Object obj = configContext.get(mobile+"lock");
        if(obj == null){
            configContext.put(mobile+"lock",new AtomicInteger());
        }
        return (AtomicInteger) configContext.get(mobile+"lock");
    }

    public VersionType getVersion(){
        VersionType defaultType = VersionType.WithReharge;
        Object obj = getConfigContext().get("pfc_version");
        if(obj == null){
            getConfigContext().put("pfc_version" , defaultType);
            return defaultType;
        }else {
            return (VersionType)obj;
        }
    }

    public boolean isOnTrans(){
        Object obj = getConfigContext().get("on_trans");
        if(obj == null){
            getConfigContext().put("on_trans", Boolean.TRUE);
            return Boolean.TRUE;
        }else {
            return (Boolean) obj;
        }
    }

    public void setOnTrans(Boolean flag){
        getConfigContext().put("on_trans", flag);
    }


}
