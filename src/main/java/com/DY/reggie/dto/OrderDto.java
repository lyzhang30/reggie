package com.DY.reggie.dto;

import com.DY.reggie.entity.OrderDetail;
import com.DY.reggie.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglianyong
 * 2022/8/322:16
 */
@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails = new ArrayList<>();

}
