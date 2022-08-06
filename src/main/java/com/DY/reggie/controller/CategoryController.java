package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.entity.Category;
import com.DY.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品和套餐分类的接口
 *@author zhanglianyong
 *@date 2022/8/5
 */
@RestController
@Slf4j
@RequestMapping("/category")
@Api(value = "菜品和套餐分类的接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品和套餐
     *
     * @author zhanglianyong
     * @date 2022/8/5 19:59
     * @param category 菜品的分类
     * @return 是否成功
     **/
    @PostMapping()
    @ApiOperation(value = "新增菜品和套餐")
    public R<String> save(@ApiParam(value = "菜品类型：菜品、套餐") @RequestBody Category category) {
        log.info(category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:00
     * @param page 页码
     * @param pageSize 页数
     * @return 菜品分类的分页信息
     **/
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品信息")
    public R<Page<Category>> getCategoryPage(@ApiParam(value = "当前页码") int page ,
                                             @ApiParam(value = "页数") int pageSize) {
        log.info("page:{},pageSize:{}", page, pageSize);
        //构造分页构器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //添加过滤条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getType, Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:02
     * @param id 菜品分类id
     * @return 是否成功
     **/
    @DeleteMapping
    @ApiOperation("根据id删除某个分类")
    public R<String> deleteById(Long id) {
        log.info("删除id:{}",id);
        // TODO 删除分类后面再做
        ///categoryService.remove(id);
        return R.success("删除成功");
    }

    /**
     * 根据id修改分类信息
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:03
     * @param category 菜品分类
     * @return 是否成功
     **/
    @PutMapping
    @ApiOperation(value = "修改分类的信息")
    public R<String> editById(@ApiParam(value = "分类信息") @RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询分类数据
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:04
     * @param category 菜品分类
     * @return 菜品的列表发信息
     **/
    @GetMapping("/list")
    @ApiOperation(value ="根据分类信息查询分类的菜品信息")
    public R<List<Category>> getList(@ApiParam(value = "分类信息") Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        //添加条件
        queryWrapper.eq(category.getType()!=null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
