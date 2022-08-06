package com.DY.reggie.service;

import com.DY.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 菜品分类服务类接口
 *@author zhanglianyong
 *@date 2022/8/6
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除菜品
     * @param id
     */
    public void remove(Long id);

}
