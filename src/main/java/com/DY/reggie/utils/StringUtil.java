package com.DY.reggie.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author zhanglianyong
 * 2022/8/422:35
 */
public class StringUtil {

    /**
     * 转换成Long类型
     * @param num 需要转的数字
     * @return long类型的数字
     */
    public static Long toLong(String num) {
         num = StringUtils.trimToNull(num);
        if(null != num && !"".equals(num)){
            return Long.valueOf(num);
        }
        return null;
    }




}
