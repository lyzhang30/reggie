package com.DY.reggie.dto;

import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.entity.SetmealDish;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.DY.reggie.entity.Setmeal;

import java.util.List;

/**
*@author zhanglianyong
*@date 2022/8/2
*/

@Data
@ApiModel("套餐Dto对象")
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
