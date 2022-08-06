package com.DY.reggie.mapper;

import com.DY.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
