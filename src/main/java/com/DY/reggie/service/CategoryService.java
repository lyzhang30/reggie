package com.DY.reggie.service;

import com.DY.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除菜品
     * @param id
     */
    public void remove(Long id);

}
