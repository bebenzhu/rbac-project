package com.ssm.rbac.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ssm.utils.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * 获取配置文件信息
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {  
  
    private static Map<String, Object> ctxPropertiesMap;  
  
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,  
            Properties props)throws BeansException {  
  
        super.processProperties(beanFactory, props);  
        ctxPropertiesMap = new HashMap<String, Object>();  
        
        //专门负责解析路径中或者名字中含有占位符的字串，并负责填充上具体的值
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
                DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX, DEFAULT_VALUE_SEPARATOR, false);
        //load properties to ctxPropertiesMap  
        for (Object key : props.keySet()) {  
            String keyStr = key.toString();  
            String value = props.getProperty(keyStr); 
            value = helper.replacePlaceholders(value, props);
            ctxPropertiesMap.put(keyStr, value);  
        }  
    }  
  
    public static Object getContextProperty(String name) {  
        return ctxPropertiesMap.get(name);  
    }  
    public static String getProperty(String name) {  
    	if("rax.biz.data.path".equals(name))
    		return (String)getContextProperty(name)+"/"/*+ DateUtils.getTodayTime("yyyyMMdd")*/;
    	else
    		return (String)getContextProperty(name);  
    }  
}   
