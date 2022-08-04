package com.DY.reggie.common;

/**
 * 基于Threadlocal封装工具类，用户保存和获取当前登录用户的id
 *
 *@author zhanglianyong
 *@date 2022/8/4
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return 返回id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
