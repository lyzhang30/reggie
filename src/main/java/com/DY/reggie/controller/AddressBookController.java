package com.DY.reggie.controller;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.R;
import com.DY.reggie.entity.AddressBook;
import com.DY.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址模块
 * @author 大勇
 */

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     * @param addressBook
     * @return
     */
    @PostMapping()
    public R<AddressBook> save (@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}",addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook>  setDefault(@RequestBody AddressBook addressBook){
        log.info("addressBook:{}",addressBook);
        LambdaUpdateWrapper<AddressBook> Wrapper = new LambdaUpdateWrapper<>();
        Wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        Wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(Wrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook != null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到对象");
        }
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if(addressBook == null){
            return R.error("没有找到该对象");
        }else{
            return R.success(addressBook);
        }

    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}",addressBook);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);


        return R.success(addressBookService.list(queryWrapper));
    }

}
