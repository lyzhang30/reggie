package com.DY.reggie.controller;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.R;
import com.DY.reggie.entity.ShoppingCart;
import com.DY.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *@author zhanglianyong
 *@date 2022/7/17
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
@Api("购物车控制类")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车列表
     * @return 购物车列表
     */
    @GetMapping("/list")
    @ApiOperation("获取购物车的列表信息")
    public R<List<ShoppingCart>> list() {
        log.info("当前用户id：{}", BaseContext.getCurrentId());
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 添加到购物车
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加倒购物车")
    public R<String> add(@ApiParam("将需要添加的数据封装成一个ShoppingCart") @RequestBody ShoppingCart shoppingCart){
        log.info("加入购物车:{}", shoppingCart.toString());
        //设置用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //查询当前菜品和当前套餐是否存在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        //添加的是菜品
        if(shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //若是存在，就在原来数量基础加1
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartServiceOne != null){
            int num = shoppingCartServiceOne.getNumber() + 1;
            shoppingCartServiceOne.setNumber(num);
        }else{
            //如果不存在，则添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }

        return R.success("添加成功");
    }

    /**
     * 清空购物车
     *
     * @author zhanglianyong
     * @date 2022/8/2 23:50
     * @return
     **/
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车");
    }

}
