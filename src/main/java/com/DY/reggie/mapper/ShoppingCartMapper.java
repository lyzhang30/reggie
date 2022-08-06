package com.DY.reggie.mapper;

import com.DY.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
