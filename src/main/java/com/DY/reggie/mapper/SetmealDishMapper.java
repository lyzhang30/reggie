package com.DY.reggie.mapper;

import com.DY.reggie.entity.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 套餐菜品持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    /**
     * 主要是为了查询禁用菜品信息时会不会出现套餐还在售卖中
     *
     * @author zhanglianyong
     * @date 2022/8/6 18:35
     * @param dishList  菜品列表
     * @return 条数
     **/
    int countSetmealDishStatus(String[] dishList);
}
