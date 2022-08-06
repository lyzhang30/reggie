package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.entity.Employee;
import com.DY.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 大勇
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api("员工登录控制类")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request 请求对象
     * @param employee 员工信息
     * @return 员工信息
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public R<Employee> login(HttpServletRequest request,@ApiParam("员工信息") @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询数据库
        LambdaQueryWrapper<Employee> querryWrapper = new LambdaQueryWrapper<>();
        querryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp =employeeService.getOne(querryWrapper);
        //判断是否登录失败
        if (null == emp) {
            return R.error("登录失败");
        }
        //密码对比，不一定返回登录结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        if (emp.getStatus() ==0) {
            return R.error("账户已禁用");
        }
        //用户登录成功
        System.out.println(request.getSession());
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request HttpServletRequest
     * @return 返回是否成功
     */
    @PostMapping("/logout")
    @ApiOperation("员工登出")
    public R<String> loginOut(HttpServletRequest request) {
        //清楚Session中保存当前员工登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee 员工信息
     * @return 返回是否成功
     */
    @PostMapping
    @ApiOperation("新增一个员工")
    public R<String> save(@ApiParam("员工的基本信息") @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        //设置初始密码，需要进行MD5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param page 页码
     * @param pageSize 页数
     * @param name 员工关键字
     * @return 分页的员工的信息
     */
    @GetMapping("/page")
    @ApiOperation("分页查询员工的基本信息")
    public R<Page<Employee>> page(@ApiParam("页码") int page, @ApiParam("页数") int pageSize, @ApiParam("关键字") String name) {
        log.info("page:{},pageSize:{},name:{}", page, pageSize, name);
        //构造分页构器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee>  querryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        querryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        querryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo, querryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee 员工信息
     * @return 返回是否成功
     */
    @PutMapping("")
    @ApiOperation("根据id修改员工信息")
    public R<String> update(@ApiParam("将员工的信息封装成一个员工类") @RequestBody Employee employee) {
        log.info(employee.toString());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id 员工id
     * @return 返回员工信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public R<Employee> getById(@ApiParam("员工的id") @PathVariable Long id) {
        log.info("根据id查询用户");
        Employee employee = employeeService.getById(id);
        if (null != employee) {
            return R.success(employee);
        }
        return null;
    }


}
