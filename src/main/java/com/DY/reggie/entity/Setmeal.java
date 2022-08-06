package com.DY.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
*@author zhanglianyong
*@date 2022/8/2
*/
@Data
@ApiModel("套餐")
public class Setmeal implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 套餐id
     */
    @ApiModelProperty("主键")
    private Long id;
    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long categoryId;
    /**
     * 套餐名称
     */
    @ApiModelProperty("套餐名称")
    private String name;
    /**
     * 套餐价格
     */
    @ApiModelProperty("套餐价格")
    private BigDecimal price;
    /**
     * 状态 0:停用 1:启用
     */
    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 编码
     */
    @ApiModelProperty("套餐编号")
    private String code;
    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    private String description;
    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String image;
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
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
