package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.dto.SetmealDto;
import com.DY.reggie.entity.Category;
import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.service.CategoryService;
import com.DY.reggie.service.SetmealDishService;
import com.DY.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐控制类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
@Api("套餐控制类")
public class SetmealController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增套餐
     * @param setmealDto 套餐Dto对象
     * @return 返回是否成功
     */
    @PostMapping
    @CacheEvict(value="setmealCache", allEntries=true)
    @ApiOperation("新增套餐")
    public R<String> addSetmeal(@ApiParam("将套餐数据封装成一个SetmealDto") @RequestBody SetmealDto setmealDto) {
        log.info("套餐：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 修改套餐数据
     *
     * @author zhanglianyong
     * @date 2022/8/2 23:52
     * @param setmealDto 套餐Dto对象
     * @return 是否成功
     **/
    @PutMapping
    @CacheEvict(value="setmealCache", allEntries=true)
    @ApiOperation("修改套餐数据")
    public R<String> editSetmeal(@ApiParam("将套餐数据封装成一个SetmealDto") @RequestBody SetmealDto setmealDto) {
        log.info("套餐：{}",setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("套餐修改成功");
    }

    /**
     * 根据套餐id查询套餐信息
     * @param id 套餐的id
     * @return 套餐Dto对象
     */
    @GetMapping("/{id}")
    @ApiOperation("根据套餐id来查询套餐信息")
    public R<SetmealDto> getSetmeal(@ApiParam("传入的套餐id，注意是在路径中传来") @PathVariable Long id){
        log.info("{}",id);
        SetmealDto setmealDto = setmealService.getSetmeal(id);
        return R.success(setmealDto);
    }

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 页数
     * @param name 关键字
     * @return 分页信息
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐数据")
    public R<Page<SetmealDto>> getPage(@ApiParam("当前页码") int page, @ApiParam("页数") int pageSize, @ApiParam("关键字") String name){
        log.info("page:{},pageSize:{},name:{}", page, pageSize, name);
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> dtoPage = new Page<>();
        //添加查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name!=null, Setmeal::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 禁用或者启用套餐
     *
     * @author zhanglianyong
     * @date 2022/8/2 23:55
     * @param status 套餐的状态
     * @param ids 套餐的id
     * @return 是否成功
     **/
    @RequestMapping("/status/{status}")
    @CacheEvict(value="setmealCache", allEntries=true)
    @ApiOperation("禁用套餐或者启用套餐")
    public R<String> updateStatus(@ApiParam("套餐状态") @PathVariable Integer status, @ApiParam("需要修改的套餐id，以,分隔") String ids){
        log.info("更新状态：{},套餐Id:{}",status,ids);
        setmealService.updateWithStatus(status,ids);
        return R.success("更改成功");
    }

    /**
     * 删除套餐
     *
     * @author zhanglianyong
     * @date 2022/8/2 23:57
     * @param ids 套餐的id
     * @return 是否成功
     **/
    @DeleteMapping
    @CacheEvict(value="setmealCache", allEntries=true)
    @ApiOperation("删除套餐")
    public R<String> delete(@ApiParam("需要删除的套餐id，以,分隔") @RequestParam List<Long> ids){
        log.info("ids：{}",ids);
        setmealService.deleteWithSetmeal(ids);
        return R.success("删除成功");
    }

    /**
     * 查询套餐列表
     * @param setmeal 套餐信息
     * @return 套餐的列表
     */
    @GetMapping("/list")
    @Cacheable(value ="setmealCache", key = "#setmeal.categoryId+'_'+#setmeal.status")
    @ApiOperation("查询套餐列表")
    public R<List<Setmeal>> list(@ApiParam("套餐的基本信息") Setmeal setmeal){
        log.info("查询套餐数据{}",setmeal);
        List<Setmeal> list;
        String key = "setmeal_"+setmeal.getCategoryId()+"_"+setmeal.getStatus();
        //从缓存中查数据
       //list = (List<Setmeal>) redisTemplate.opsForValue().get(key);
       //if(list !=null){
       //    return R.success(list);
       //}
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!= null, Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        list = setmealService.list(queryWrapper);
        //添加到缓存
        //redisTemplate.opsForValue().set(key,list,5, TimeUnit.MINUTES);
        return R.success(list);
    }

    /**
     * 查询套餐信息
     * @param id 套餐的id
     * @return 套餐的列表
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据id查询套餐信息")
    public R<List<Setmeal>> getDishList(@ApiParam("套餐的id") @PathVariable Long id){
        log.info("查询套餐信息");
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq( Setmeal::getStatus,1);
        queryWrapper.eq(id != null,Setmeal::getId, id);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }


}
