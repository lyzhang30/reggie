package com.DY.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhanglianyong
 * @date 2022/8/2 23:36
 **/
@Data
@ApiModel("套餐的菜品详情实体")
public class SetmealDish implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 套餐id
     */
    private Long setmealId;
    /**
     * 菜品id
     */
    private Long dishId;
    /**
     * 菜品名称 （冗余字段）
     */
    private String name;
    /**
     * 菜品原价
     */
    private BigDecimal price;
    /**
     * 份数
     */
    private Integer copies;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 是否删除
     */
    private Integer isDeleted;

}
