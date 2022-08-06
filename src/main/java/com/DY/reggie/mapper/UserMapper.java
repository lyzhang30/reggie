package com.DY.reggie.mapper;

import com.DY.reggie.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
