package com.DY.reggie.service.impl;

import com.DY.reggie.entity.Employee;
import com.DY.reggie.mapper.EmployeeMapper;
import com.DY.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 员工信息服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
