package com.DY.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
*@author zhanglianyong
*@date 2022/8/2
*/
@Data
@ApiModel("菜品口味明细")
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 明细id
     */
    @ApiModelProperty("明细id")
    private Long id;
    /**
     * 菜品id
     */
    @ApiModelProperty("菜品id")
    private Long dishId;
    /**
     * 口味名称
     */
    @ApiModelProperty("口味名称")
    private String name;
    /**
     * 口味数据list
     */
    @ApiModelProperty("口味数据list")
    private String value;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Integer isDeleted;

}
