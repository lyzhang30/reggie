package com.DY.reggie.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 * 日期工具类
 * @author zhanglianyong
 * 2022/8/422:46
 */
public class DateUtils {


    public static LocalDateTime formatDateTime(String dateTime) {
        dateTime = StringUtils.trimToNull(dateTime);
        if(dateTime != null && !"".equals(dateTime)) {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }

    public static LocalDateTime getLocalDateTime(String time) {
        return LocalDateTime.now();
    }
}
