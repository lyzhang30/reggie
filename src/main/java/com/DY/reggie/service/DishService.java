package com.DY.reggie.service;

import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface DishService extends IService<Dish> {

    /**
     * 保存菜品信息
     *
     * @author zhanglianyong
     * @date 2022/8/4 23:33
     * @param dishDto 菜品信息
     **/
    void saveWithFlavor(DishDto dishDto);

    /**
     * 更新菜品信息，需要更新菜品和口味表
     * @param dishDto 菜品信息
     */
    void updateWithFlavor(DishDto dishDto);
    /**
     * 根据id查询菜品和对应的口味信息
     * @param id 菜品的id
     * @return DishDto对象
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 批量禁用菜品
     * @param status 菜品的状态
     * @param ids 需要禁用的菜品id,多个以逗号分隔
     */
    void updateStatus(Integer status,String ids);

    /**
     * 批量删除菜品
     * @param ids 需要删除的菜品id,多个以逗号分隔
     */
    void deleteFood(List<Long> ids);

}
