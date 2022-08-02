package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.entity.Category;
import com.DY.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
@Api(value = "菜品和套餐接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 新增菜品和套餐
     * @param category
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "新增菜品和套餐")
    public R<String> save(@ApiParam(value = "菜品类型：菜品、套餐") @RequestBody Category category) {
        log.info(category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品信息")
    public R<Page<Category>> getCategoryPage(@ApiParam(value = "当前页码") int page ,
                                             @ApiParam(value = "页数") int pageSize){
        log.info("page:{},pageSize:{}",page,pageSize);
        //构造分页构器
        Page pageInfo = new Page(page,pageSize);
        //添加过滤条件
        LambdaQueryWrapper<Category> querryWrapper = new LambdaQueryWrapper<Category>();
        //添加排序条件
        querryWrapper.orderByAsc(Category::getType,Category::getSort);
        categoryService.page(pageInfo,querryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(Long id){
        log.info("删除id:{}",id);
        ///categoryService.remove(id);
        return R.success("删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改分类的信息")
    public R<String> editById(@ApiParam(value = "分类信息") @RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value ="根据分类信息查询分类的菜品信息")
    public R<List<Category>> getList(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
