package com.DY.reggie.dto;

import com.DY.reggie.entity.OrderDetail;
import com.DY.reggie.entity.Orders;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglianyong
 * 2022/8/322:16
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("订单Dto对象")
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails = new ArrayList<>();

}
