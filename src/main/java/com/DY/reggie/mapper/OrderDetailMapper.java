package com.DY.reggie.mapper;

import com.DY.reggie.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单详情持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
