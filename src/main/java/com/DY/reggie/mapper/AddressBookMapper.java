package com.DY.reggie.mapper;

import com.DY.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址持久化Mapper
 *@author zhanglianyong
 *@date 2022/8/6
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
