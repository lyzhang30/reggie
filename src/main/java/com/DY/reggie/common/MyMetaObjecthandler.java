package com.DY.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充字段
 *
 *@author zhanglianyong
 *@date 2022/8/5
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     *
     * @author zhanglianyong
     * @date 2022/8/5 0:02
     * @param metaObject 元数据对象
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");


        metaObject.setValue("createTime", LocalDateTime.now());

        metaObject.setValue("updateTime",LocalDateTime.now());

        metaObject.setValue("createUser",BaseContext.getCurrentId());


        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     * 更新操作，自动填充
     *
     * @author zhanglianyong
     * @date 2022/8/5 0:03
     * @param metaObject 元数据对象
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
