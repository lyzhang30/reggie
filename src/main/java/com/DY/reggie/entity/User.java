package com.DY.reggie.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhanglianyong
 * @date 2022/8/2 23:38
 **/
@Data
@ApiModel("用户实体类")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 性别 0:女 1:男
     */
    @ApiModelProperty("性别 0:女 1:男")
    private String sex;
    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idNumber;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 状态 0:禁用，1:正常
     */
    @ApiModelProperty("状态 0:禁用，1:正常")
    private Integer status;

}
