package com.DY.reggie.service;

import com.DY.reggie.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 套餐菜品服务类接口
 *@author zhanglianyong
 *@date 2022/8/6
 */
public interface SetmealDishService extends IService<SetmealDish> {
    /**
     * 主要是为了查询禁用菜品信息时会不会出现套餐还在售卖中
     *
     * @author zhanglianyong
     * @date 2022/8/6 18:31
     * @param dishList  dishid的列表
     * @return 条数
     **/
    int countSetmealDishStatus(String[] dishList);
}
