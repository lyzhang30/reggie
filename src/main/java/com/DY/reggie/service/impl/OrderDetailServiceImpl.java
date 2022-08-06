package com.DY.reggie.service.impl;

import com.DY.reggie.mapper.OrderDetailMapper;
import com.DY.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单详情服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, com.DY.reggie.entity.OrderDetail> implements OrderDetailService {

}
