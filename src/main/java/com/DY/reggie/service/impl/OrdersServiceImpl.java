package com.DY.reggie.service.impl;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.CustomException;
import com.DY.reggie.entity.*;
import com.DY.reggie.mapper.OrdersMapper;
import com.DY.reggie.service.*;
import com.DY.reggie.utils.DateUtils;
import com.DY.reggie.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 订单服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 将购物车的菜品和套餐下单并删除掉，在订单表和订单详情表中添加对应的详情
     *
     * @author zhanglianyong
     * @date 2022/8/4 22:03
     * @param orders 订单信息
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Orders orders) {
        //当前用户id
        Long userId = BaseContext.getCurrentId();
        //当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if(shoppingCarts == null || shoppingCarts.size() ==0){
            throw new CustomException("购物车为空，不能下单");
        }
        //查询用户数据
        User user = userService.getById(userId);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }
        //原子变量，总金额
        AtomicInteger amount = new AtomicInteger(0);
        //订单编号，插入数据
        long orderId = IdWorker.getId();

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber() )).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setNumber(String.valueOf(orderId));
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress((addressBook.getProvinceName() == null ? "" :addressBook.getProvinceName())
        + (addressBook.getCityName() == null ? "" : addressBook.getCityName() )
        + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
        + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        orders.setId(orderId);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setStatus(2);

        //明细表，插入数据（可能多条）
        this.save(orders);
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(queryWrapper);

    }

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
    @Override
    public Page<Orders> page(int page, int pageSize, String orderId, String beginTime, String endTime) {
        Page<Orders> orders = new Page<>(page, pageSize);

        LocalDateTime beginTimeCopy = DateUtils.formatDateTime(beginTime);
        LocalDateTime endTimeCopy = DateUtils.formatDateTime(endTime);
        // 查询订单信息
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long orderIdCopy = StringUtil.toLong(orderId);
        queryWrapper.eq(orderIdCopy != null, Orders::getId, orderIdCopy);
        if(null != beginTimeCopy && null != endTimeCopy) {
            queryWrapper.between(Orders::getOrderTime, beginTimeCopy, endTimeCopy);
        }
        this.page(orders, queryWrapper);

        return orders;
    }

    /**
     * 修改订单的派送状态
     *
     * @author zhanglianyong
     * @date 2022/8/4 23:08
     * @param orders 订单信息
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Orders orders) {
        if(null == orders) {
            throw new CustomException("请选择需要更新的订单记录");
        }
        this.updateById(orders);
    }
}
