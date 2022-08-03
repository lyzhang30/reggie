package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Category;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.DishFlavor;
import com.DY.reggie.service.CategoryService;
import com.DY.reggie.service.DishFlavorService;
import com.DY.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
@Api("菜品控制类")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public R<String> saveFood(@ApiParam("菜品的信息封装成Dto") @RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

        ////清除所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //清除某个分类下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 根据分页查询数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品数据")
    public R<Page> getPage(@ApiParam("当前页码") int page,@ApiParam("页数") int pageSize,@ApiParam("关键字") String name){
        //构造分页查询对象
        Page<Dish> pageInfo = new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息和口味信息")
    public R<DishDto> getWithFlavor(@ApiParam(value = "菜品的id",required = true) @PathVariable Long id ){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新、修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @ApiOperation("更新、修改菜品信息")
    public R<String> updateFood(@ApiParam("将菜品信息封装成一个Dto") @RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        //删除缓存中的数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("修改菜品成功");
    }

    /**
     * 更新菜品的状态
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("批量禁用和启用菜品")
    public R<String> updateFoodStatus(@ApiParam(value = "菜品的状态", required = true) @PathVariable Integer status,
                                      @ApiParam(value = "传入的菜品，以,分隔",required = true) String ids){
        log.info("status:{},ids:{}",status,ids);
        dishService.updateStatus(status,ids);
        //删除缓存中的数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("禁用成功");
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public R<String> deleteFood( @RequestParam List<Long> ids){
        log.info("删除的ids:{}",ids);
        return null;
    }

    /**
     * 根据菜品类别查询数据
     * @param dish
     * @return
     */
    //@GetMapping("/list")
    //public R<List<Dish>> list(Dish dish){
    //    log.info("categoryId:{}",dish.toString());
    //    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //
    //    queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
    //    queryWrapper.eq(Dish::getStatus,1);
    //
    //    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //
    //    List<Dish> list = dishService.list(queryWrapper);
    //
    //    return R.success(list);
    //}
    @GetMapping("/list")
    @ApiOperation("根据分页查询菜品")
    public R<List<DishDto>> list(@ApiParam("菜品信息") Dish dish){
        log.info("categoryId:{}",dish.toString());
        List<DishDto> dishDtoList = null;
        //添加到Redis中，使用Redis缓存
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        //从缓存中获取数据库
        dishDtoList = (List<DishDto>)redisTemplate.opsForValue().get(key);
        if(dishDtoList != null){
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在的话，把数据加入到缓存中
        try{
            redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        }catch(Exception e){
            log.info("菜品加入缓存失败,报错{}",e);
        }
        return R.success(dishDtoList);

    }
}
