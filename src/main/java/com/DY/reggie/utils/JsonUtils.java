package com.DY.reggie.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;

/**
 * json 工具类
 * @author zhanglianyong
 * 2022/8/1015:30
 */
public class JsonUtils {
    /**
     * 用来将对象转成json字符串，忽略为null的属性
     *
     * @author zhanglianyong
     * @date 2022/8/10
     * @param object  需要转成json字符串的对象
     * @return json字符串
     **/
    private static String toJsonStringNotNull(Object object) {
        if (null == object) {
            return null;
        }
        String jsonString = JSONObject.toJSONString(object, SerializerFeature.PrettyFormat);
        return jsonString;
    }

    /**
     *
     *
     * @author zhanglianyong
     * @date 2022/8/10
     * @param object 需要转成json字符串的对象
     * @param properties 需要的列
     * @return 转成的json字符串
     **/
    private static String toJsonStringIncludeProperties(Object object, String[] properties) {
        if (null == object) {
            return null;
        }
        PropertyPreFilters propertyPreFilters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter include = propertyPreFilters.addFilter();
        include.addIncludes(properties);
        return JSONObject.toJSONString(object, include, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
    }

    private static String toJsonStringExcludeSomeProperties(Object object, String[] properties) {
        if (null == object) {
            return null;
        }
        //创建PropertyPreFilters对象
        PropertyPreFilters propertyPreFilters = new PropertyPreFilters();
        //添加要排除的属性
        PropertyPreFilters.MySimplePropertyPreFilter exclude = propertyPreFilters.addFilter();
        exclude.addExcludes(properties);
        // SerializerFeature.WriteMapNullValue 可以设置是否包含null
        String jsonString = JSONObject.toJSONString(object, exclude, SerializerFeature.PrettyFormat);
        return jsonString;
    }
}
