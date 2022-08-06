package com.DY.reggie.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

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
        if (null != num && !"".equals(num)) {
            return Long.valueOf(num);
        }
        return null;
    }

    /**
     * 将字符串数组转成Long列表
     * @param ids
     * @return
     */
    public static List<Long> toLongList(String[] ids) {
        if (ids.length<=0) {
            return null;
        }

        List<Long> ret = new LinkedList<>();
        for(String id: ids){
             ret.add(toLong(id));
        }
        return ret;
    }

    /**
     * long转成String数组
     * @param ids
     * @return
     */
    public static String[] longToStringArray(List<Long> ids) {
        String[] ret = new String[ids.size()];
        int index =0;
        for (Long i : ids) {
            ret[index++] = longToString(i);
        }
        return ret;
    }

    /**
     * long转成String类型
     * @param num
     * @return
     */
    public static String longToString(Long num) {
        return String.valueOf(num);
    }
}
