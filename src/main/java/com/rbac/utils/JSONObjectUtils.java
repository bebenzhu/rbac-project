package com.rbac.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONObjectUtils {


    public static String toJSONString(Object object){
        //日期的格式化处理
        SerializeConfig slizeCfg = new SerializeConfig();
        slizeCfg.put(Date.class, new SimpleDateFormatSerializer("yyyy/MM/dd HH:mm:ss.SSS"));
        String str = JSON.toJSONString(object,
                slizeCfg,
                new SerializeFilter[]{},
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNonStringKeyAsString,
                SerializerFeature.SortField);
        return str;
    }

    /**
     * 字符串转map
     * @param text
     * @return
     */
    public static Map<String,Object> toMap(String text){
        return new LinkedHashMap<String,Object>(toJSONObject(text));
    }

    /**
     * 对象转map
     * @param object
     * @return
     */
    public static Map<String,Object> toMap(Object object){
        return new LinkedHashMap<String,Object>(toJSONObject(object));
    }

    /**
     * 字符串转json对象
     * @param text
     * @return
     */
    public static JSONObject toJSONObject(String text){
        return JSON.parseObject(text, Feature.OrderedField);
    }

    /**
     * 对象转json对象
     * @param object
     * @return
     */
    public static JSONObject toJSONObject(Object object){
        return JSON.parseObject(toJSONString(object), Feature.OrderedField);
    }

    public static <T> List<T> toArrayObject(String text, Class<T> requiredType) {
        return JSON.parseArray(text, requiredType);
    }
}
