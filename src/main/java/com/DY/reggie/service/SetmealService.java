package com.DY.reggie.service;

import com.DY.reggie.dto.SetmealDto;
import com.DY.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存与菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 修改套餐，同时需要删除原有的数据，再重新插入修改后的数据
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
    /**
     * 批量停售套餐，需要更新status
     */
    public void updateWithStatus(Integer status, String ids);

    /**
     * 批量删除套餐，需要判断当前套餐关联的数据
     * @param ids
     */
    public void deleteWithSetmeal(List<Long> ids);

    public SetmealDto getSetmeal(Long id);

}
