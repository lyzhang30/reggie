package com.DY.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 捕获全局异常
 *
 *@author zhanglianyong
 *@date 2022/8/4
 */
@ControllerAdvice(annotations ={RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 异常处理方法，防止重复插入
     *
     * @author zhanglianyong
     * @date 2022/8/4 23:59
     * @param ex  异常信息
     * @return 返回是否成功
     **/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] msg = ex.getMessage().split(" ");
            String userName = msg[2];
            String err = userName+"已存在";
            return R.error(err);
        }
        return R.error("未知错误");
    }


    /**
     * 捕获异常信息，业务异常
     *
     * @author zhanglianyong
     * @date 2022/8/5 0:00
     * @param ex CustomException
     * @return 是否成功
     **/
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

}
