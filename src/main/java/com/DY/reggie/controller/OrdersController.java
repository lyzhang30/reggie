package com.DY.reggie.controller;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.R;
import com.DY.reggie.dto.OrderDto;
import com.DY.reggie.entity.OrderDetail;
import com.DY.reggie.entity.Orders;
import com.DY.reggie.service.OrderDetailService;
import com.DY.reggie.service.OrdersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
*@author zhanglianyong
*@date 2022/8/3
*/      
@RestController
@Slf4j
@RequestMapping("/order")
@ApiModel("订单控制类")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 下单
     *       
     * @author zhanglianyong
     * @date 2022/8/3 10:22 
     * @param orders       
     * @return 
     **/       
    @PostMapping("/submit")
    @ApiOperation("下单，提交订单信息")
    public R<String> submit(@ApiParam("订单信息") @RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("userPage")
    @ApiOperation("分页查询订单信息")
    public R<Page> page(int page, int pageSize){
        log.info("page:{},pageSize:{}",page,pageSize);
        Page<Orders> orderPage= new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> orderQueeryWrapper = new LambdaQueryWrapper<>();
        orderQueeryWrapper.eq(Orders::getUserId,userId);
        orderQueeryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(orderPage, orderQueeryWrapper);
        //查询对应的订单明细
        BeanUtils.copyProperties(orderPage, orderDtoPage,"records");
        List<Orders> records = orderPage.getRecords();
        List<OrderDto> orderDtoList = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            Long orderId = orderDto.getId();
            LambdaQueryWrapper<OrderDetail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(orderId != null, OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailQueryWrapper);
            if (orderDetails.size() > 0) {
                orderDto.setOrderDetails(orderDetails);
            }
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(orderDtoList);

        return R.success(orderDtoPage);
    }

}
