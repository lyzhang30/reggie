package com.DY.reggie.service.impl;

import com.DY.reggie.common.CustomException;
import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.DishFlavor;
import com.DY.reggie.entity.SetmealDish;
import com.DY.reggie.mapper.DishFlavorMapper;
import com.DY.reggie.mapper.DishMapper;
import com.DY.reggie.service.*;
import com.DY.reggie.utils.StringUtil;
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
 * 菜品服务类接口实现
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品，同时添加口味，需要添加事物支持
     * @param dishDto 菜品Dto对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.dishMapper.insert(dishDto);
        log.info("dishDto:{}",dishDto.toString());
        //获取菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 更新dish和对应口味信息
     * @param dishDto dishDto对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加更新后的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
         
    }

    /**
     * 根据id查询菜品和对应的口味信息
     * @param id 菜品的id
     * @return 菜品Dto对象
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //从dish表开始查
        Dish dish = this.getById(id);
        Long dishId = dish.getId();

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前口味信息

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dishId);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     *  批量禁用商品
     * @param status 菜品状态
     * @param ids 菜品的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Integer status, String ids) {
        String[] idsList = ids.split(",");
        if(idsList.length <= 0){
            throw new CustomException("传入id不能为空");
        }
        int record = this.setmealDishService.countSetmealDishStatus(idsList);
        if (record > 0) {
            throw new CustomException("包含该菜品的套餐正在售卖中，请联系管理员暂停售卖该套餐");
        }

        for (String id :idsList) {
            Long idL = Long.valueOf(id);
            LambdaQueryWrapper<SetmealDish>  setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getDishId, idL);
            Dish dish = this.getById(idL);
            dish.setStatus(status);
            dishMapper.updateById(dish);
        }
    }

    /**
     * 批量删除菜品
     * @param idLongList 菜品的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFood(List<Long> idLongList) {

        //先判断当前菜品是否在售，可以的话
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, idLongList);
        queryWrapper.eq(Dish::getStatus,1);
        int count = this.count(queryWrapper);
        if(count >0 ){
            throw new CustomException("该菜品正在售卖中，不能删除");
        }
        String[] idsList = StringUtil.longToStringArray(idLongList);
        int record = this.setmealDishService.countSetmealDishStatus(idsList);
        if (record > 0) {
            throw new CustomException("包含该菜品的套餐正在售卖中，请联系管理员暂停售卖该套餐");
        }
        this.removeByIds(idLongList);
        //删除对应的口味表

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, idLongList);
        dishFlavorMapper.delete(dishFlavorLambdaQueryWrapper);
    }



}
