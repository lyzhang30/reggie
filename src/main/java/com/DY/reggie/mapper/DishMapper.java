package com.DY.reggie.mapper;

import com.DY.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
