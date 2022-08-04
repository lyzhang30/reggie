package com.DY.reggie.common;

/**
 * 基础异常
 *
 *@author zhanglianyong
 *@date 2022/8/4
 */
public class CustomException extends RuntimeException {

    /**
     * new一个CustomException
     *
     * @author zhanglianyong
     * @date 2022/8/5 0:00
     * @param message 报错信息
     **/
    public CustomException(String message) {
        super(message);
    }

}
