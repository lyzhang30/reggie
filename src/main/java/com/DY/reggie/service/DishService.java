package com.DY.reggie.service;

import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService extends IService<Dish> {
    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要同时操作两张表
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 更新菜品信息，需要更新菜品和口味表
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);
    /**
     * 根据id查询菜品和对应的口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 批量禁用菜品
     * @param status
     * @param ids
     */
    public void updateStatus(Integer status,String ids);

    /**
     * 批量删除菜品
     * @param ids
     */
    public void deleteFood(List<Long> ids);

}
