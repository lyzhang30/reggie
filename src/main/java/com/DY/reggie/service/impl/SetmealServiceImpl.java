package com.DY.reggie.service.impl;

import com.DY.reggie.common.CustomException;
import com.DY.reggie.dto.SetmealDto;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.entity.SetmealDish;
import com.DY.reggie.mapper.SetmealMapper;
import com.DY.reggie.service.SetmealDishService;
import com.DY.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 保存套餐，同时需要保存套餐和菜品之间的关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 停售和启售套餐
     * @param status
     * @param ids
     */
    @Override
    @Transactional
    public void updateWithStatus(Integer status, String ids) {
        String [] idsList = ids.split(",");
        if(idsList.length >0){
            for(String id :idsList){
                Long idL = Long.valueOf(id);
                Setmeal setmeal = this.getById(idL);
                setmeal.setStatus(status);
                this.save(setmeal);
            }
        }
    }

    /**
     * 批量删除套餐，需要判断当前套餐关联的数据
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithSetmeal(List<Long> ids) {
        //查询套餐是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count();
        if(count >0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，可以先删除套餐表的数据
        this.removeByIds(ids);

        //删除关联数据

        LambdaQueryWrapper<SetmealDish> lambdaQuery = new LambdaQueryWrapper<SetmealDish>();
        lambdaQuery.in(SetmealDish::getSetmealId,ids);
        //删除关系表的数据
        setmealDishService.remove(lambdaQuery);

    }


}
