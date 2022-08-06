package com.DY.reggie.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 *
 * @author zhanglianyong
 * @date 2022/8/2 23:36
 **/
@Data
@ApiModel("购物车")
public class ShoppingCart  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 菜品id
     */
    @ApiModelProperty("菜品id")
    private Long dishId;
    /**
     * 套餐id
     */
    @ApiModelProperty("套餐id")
    private Long setmealId;
    /**
     * 口味
     */
    @ApiModelProperty("口味")
    private String dishFlavor;
    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;
    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal amount;
    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String image;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
