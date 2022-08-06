package com.DY.reggie.service.impl;

import com.DY.reggie.entity.SetmealDish;
import com.DY.reggie.mapper.SetmealDishMapper;
import com.DY.reggie.service.SetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 套餐菜品服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public int countSetmealDishStatus(String[] dishList) {
        int ret = this.setmealDishMapper.countSetmealDishStatus(dishList);
        return ret;
    }
}
