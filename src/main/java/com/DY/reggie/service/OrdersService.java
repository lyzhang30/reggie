package com.DY.reggie.service;

import com.DY.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    /**
     * 下单
     * @param orders 订单信息
     */
    void submit(Orders orders);

    /**
     * 查询订单的分页信息
     *
     * @author zhanglianyong
     * @date 2022/8/4 22:30
     * @param page 页码
     * @param pageSize 页数
     * @param orderId 订单Id
     * @param beginTime 订单开始时间
     * @param endTime 订单结束时间
     * @return 返回分页信息
     **/
    Page<Orders> page(int page, int pageSize, String orderId, String beginTime, String endTime);

    /**
     * 修改订单的派送状态
     *
     * @author zhanglianyong
     * @date 2022/8/4 23:07
     * @param orders 订单信息
     **/
    void updateStatus(Orders orders);
}
