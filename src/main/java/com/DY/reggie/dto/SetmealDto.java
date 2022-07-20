package com.DY.reggie.dto;

import com.DY.reggie.entity.Setmeal;
import com.DY.reggie.entity.SetmealDish;
import lombok.Data;
import com.DY.reggie.entity.Setmeal;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
