package com.DY.reggie.service.impl;

import com.DY.reggie.entity.AddressBook;
import com.DY.reggie.mapper.AddressBookMapper;
import com.DY.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
