package com.DY.reggie.service.impl;

import com.DY.reggie.common.CustomException;
import com.DY.reggie.entity.Category;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.mapper.CategoryMapper;
import com.DY.reggie.service.CategoryService;
import com.DY.reggie.service.DishService;
import com.DY.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜品分类服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除菜品，删除前需要进行判断
     * @param id 菜品的id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否已经关联了菜品，如果已经关联，抛出一个异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count();

        //查询当前分类是否已经关联了菜品，如果已经关联，抛出一个异常
        if(count1 >0){
            //关联菜品，抛出异常
            throw new CustomException("当前分类下关联了菜品， 不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if(count2 >0){
            //关联套餐，抛出异常
            throw new CustomException("当前分类下关联了套餐， 不能删除");
        }

        //正常删除分类

        super.removeById(id);

    }

}
