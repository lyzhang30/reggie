package com.DY.reggie.service.impl;

import com.DY.reggie.common.CustomException;
import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.DishFlavor;
import com.DY.reggie.mapper.DishFlavorMapper;
import com.DY.reggie.mapper.DishMapper;
import com.DY.reggie.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 大勇
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品，同时添加口味，需要添加事物支持
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.dishMapper.insert(dishDto);
        log.info("dishDto:{}",dishDto.toString());
        //获取菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 更新dish和对应口味信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加更新后的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
         
    }

    /**
     * 根据id查询菜品和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //从dish表开始查
        Dish dish = this.getById(id);
        Long dishId = dish.getId();

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前口味信息

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();

        queryWrapper.eq(DishFlavor::getDishId,dishId);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     *  批量禁用商品
     * @param status
     * @param ids
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, String ids) {
        String [] idsList = ids.split(",");
        if(idsList.length >0){
            for(String id :idsList){
                Long idL = Long.valueOf(id);
                Dish dish = this.getById(idL);
                dish.setStatus(status);
                dishMapper.updateById(dish);
            }
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteFood(List<Long> ids) {
       //先判断当前菜品是否在售，可以的话
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count = this.count();
        if(count >0 ){
            throw new CustomException("该菜品正在售卖中，不能删除");
        }
        this.removeByIds(ids);
        //删除对应的口味表
        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<DishFlavor>();


    }



}
