package com.DY.reggie.mapper;

import com.DY.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
