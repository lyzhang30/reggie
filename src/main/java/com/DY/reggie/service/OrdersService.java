package com.DY.reggie.service;

import com.DY.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    /**
     * 下单
     * @param orders
     */
    public void submit(Orders orders);
}
