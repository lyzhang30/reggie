package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.entity.Orders;
import com.DY.reggie.service.OrdersService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
