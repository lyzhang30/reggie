package com.DY.reggie.service.impl;

import com.DY.reggie.entity.User;
import com.DY.reggie.mapper.UserMapper;
import com.DY.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
