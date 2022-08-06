package com.DY.reggie.service.impl;

import com.DY.reggie.entity.ShoppingCart;
import com.DY.reggie.mapper.ShoppingCartMapper;
import com.DY.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 购物车服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
