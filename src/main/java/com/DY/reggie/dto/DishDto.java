package com.DY.reggie.dto;

import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于新增菜品的适配数据对象
 * @author 大勇
 */
@Data
@ApiModel("菜品Dto对象")
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<DishFlavor>();

    private String CategoryName;

    private Integer copies;
}
