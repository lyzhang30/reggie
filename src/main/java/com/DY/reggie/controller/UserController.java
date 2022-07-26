package com.DY.reggie.controller;

import com.DY.reggie.common.R;
import com.DY.reggie.entity.User;
import com.DY.reggie.service.UserService;
import com.DY.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块
 */


@RestController
@Slf4j
@RequestMapping("/user")
@ApiOperation("用户类")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取验证码
     * @param user
     * @param session
     * @return
     */
    @ApiOperation(value = "获取验证码")
    @PostMapping(value = "/sendMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public R<String> sendMsg(@ApiParam(value ="用户信息") @RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //将验证码保存到Redis中，设置有效器为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            log.info("验证码：{}", redisTemplate.opsForValue().get(phone));
            return R.success("短信发送成功");
        }
        return  R.error("短信发送失败");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
     public R<User> login(@ApiParam("将登录信息封装成一个Map对象，包括code,phone") @RequestBody Map map, HttpSession session){
         log.info("map:{}", map.toString());
         String code = map.get("code").toString();
         //获取手机号
        String phone = map.get("phone").toString();
        /* 从session获取验证码
         Object codeInSession = session.getAttribute(phone);*/
        Object codeInSession  = redisTemplate.opsForValue().get(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            // 如果能够对比成功，说明登录成功
            // 如果当前用户为新用户，自动为该用户进行注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //如果登录成功，从Redis删除验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone, phone);

        User user = userService.getOne(queryWrapper);
        if (user != null) {
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());
        return R.success(user);
     }
     /**
      * 退出登录
      *
      * @author zhanglianyong
      * @date 2022/8/6 18:04
      * @param request   请求
      * @return 返回是否成功
      **/
     @PostMapping("loginout")
     @ApiOperation("退出登录")
     public R<String> loginout(HttpServletRequest request){
         request.getSession().removeAttribute("user");
         return R.success("退出成功");
     }
}
