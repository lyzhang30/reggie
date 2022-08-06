package com.DY.reggie.service.impl;

import com.DY.reggie.entity.AddressBook;
import com.DY.reggie.mapper.AddressBookMapper;
import com.DY.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 地址服务类接口实现类
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
