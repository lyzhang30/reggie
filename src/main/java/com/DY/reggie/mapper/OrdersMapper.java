package com.DY.reggie.mapper;

import com.DY.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
