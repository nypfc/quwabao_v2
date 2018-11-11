package com.gedoumi.quwabao.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    private static PropertiesUtils instance = new PropertiesUtils();



    private Properties properties;

    private PropertiesUtils() {
        try {
             properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesUtils getInstance(){
        if(instance == null){
            instance = new PropertiesUtils();
        }
        return instance;
    }

    public String getValue(String key){
        return properties.getProperty(key);
    }


}
