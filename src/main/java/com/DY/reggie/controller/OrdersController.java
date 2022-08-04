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
     * @param orders 订单信息
     * @return 返回成果
     **/       
    @PostMapping("/submit")
    @ApiOperation("下单，提交订单信息")
    public R<String> submit(@ApiParam("订单信息") @RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 分页查询订单信息
     *
     * @author zhanglianyong
     * @date 2022/8/4 21:53
     * @param page 页码
     * @param pageSize  页数
     * @return Page分页信息
     **/
    @GetMapping("userPage")
    @ApiOperation("分页查询订单信息")
    public R<Page<OrderDto>> page(@ApiParam(value = "页码", required = true) int page, int pageSize) {
        log.info("page:{},pageSize:{}",page,pageSize);
        Page<Orders> orderPage= new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();
        Long userId = BaseContext.getCurrentId();
        // 查询订单信息
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

    /**
     * 分页查询订单数据
     *
     * @author zhanglianyong
     * @date 2022/8/4 22:17
     * @param page 页码
     * @param pageSize 页数
     * @param orderId 订单号
     * @param beginTime 订单开始时间
     * @param endTime  订单结束时间
     * @return 返回Orders的分页信息
     **/
    @GetMapping("page")
    @ApiOperation(value = "分页查询订单信息", response = OrderDto.class)
    public R<Page<Orders>> page(
            @ApiParam(value = "页码") int page,
            @ApiParam(value = "页数") int pageSize,
            @ApiParam(value = "订单编号") @RequestParam(value = "number",defaultValue = "") String orderId,
            @ApiParam(value = "订单开始时间") String beginTime,
            @ApiParam(value = "订单结束时间") String endTime) {
        log.info("页码：{}，页数：{}，订单编号：{}，订单开始时间：{}，订单结束时间：{}", page, pageSize, orderId, beginTime, endTime);
        Page<Orders> orders  = ordersService.page(page, pageSize, orderId, beginTime, endTime);
        return R.success(orders);
    }

    /**
     * 修改订单的状态信息
     *
     * @author zhanglianyong
     * @date 2022/8/4 23:22
     * @param orders 订单状态信息
     * @return 返回是否成功
     **/
    @PutMapping()
    @ApiOperation("修改订单的派送状态")
    public R<String> putStatus(
            @RequestBody Orders orders) {
        ordersService.updateStatus(orders);
        return R.success("修改成功");
    }
}
