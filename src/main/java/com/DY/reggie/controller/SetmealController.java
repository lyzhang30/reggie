package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.dto.SetmealDto;
import com.DY.reggie.entity.Category;
import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.entity.SetmealDish;
import com.DY.reggie.service.CategoryService;
import com.DY.reggie.service.SetmealDishService;
import com.DY.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("套餐：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> getPage(int page, int pageSize, String name){
        log.info("page:{},pageSize:{},name:{}",page,pageSize,name);
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> dtoPage = new Page<>();
        //添加查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name!=null, Setmeal::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

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

    @RequestMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,String ids){
        log.info("更新状态：{},套餐Id:{}",status,ids);
        setmealService.updateWithStatus(status,ids);
        return R.success("更改成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids：{}",ids);
        setmealService.deleteWithSetmeal(ids);
        return R.success("删除成功");
    }

    /**
     * 查询套餐列表
     * @param setmeal
     * @return
     */
    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        log.info("查询套餐数据{}",setmeal);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        queryWrapper.eq(setmeal.getCategoryId()!= null, Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<Setmeal>> getDishList(@PathVariable Long id){
        log.info("查询套餐信息");
        Long setmealId = id;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        queryWrapper.eq( Setmeal::getStatus,1);
        queryWrapper.eq(setmealId!= null,Setmeal::getId,setmealId);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }


}
