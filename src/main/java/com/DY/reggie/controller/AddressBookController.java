package com.DY.reggie.controller;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.R;
import com.DY.reggie.entity.AddressBook;
import com.DY.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址模块
 *
 *@author zhanglianyong
 *@date 2022/8/5
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
@ApiModel(value ="地址信息")
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * 新增地址信息
     *
     * @author zhanglianyong
     * @date 2022/8/5 19:53
     * @param addressBook  地址信息
     * @return 地址信息
     **/
    @PostMapping()
    @ApiOperation("新增地址")
    public R<AddressBook> save (@ApiParam("地址信息") @RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 保存修改过后的信息
     *
     * @author zhanglianyong
     * @date 2022/8/3 21:53
     * @param addressBook 地址信息
     * @return 返回地址信息
     **/
    @PutMapping()
    @ApiOperation("保修修改后的地址")
    public R<AddressBook> update(@ApiParam("地址信息") @RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

   /**
    * 设置默认地址
    *
    * @author zhanglianyong
    * @date 2022/8/5 19:54
    * @param addressBook 地址信息
    * @return 地址信息
    **/
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public R<AddressBook>  setDefault(@ApiParam("地址信息") @RequestBody AddressBook addressBook) {
        log.info("addressBook:{}",addressBook);
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    /**
     * 根据id查询地址
     *
     * @author zhanglianyong
     * @date 2022/8/5 19:55
     * @param id 地址id
     * @return 地址
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址信息")
    public R<AddressBook> get(@ApiParam("地址信息的id") @PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到对象");
        }
    }

    /**
     * 查询默认地址
     *
     * @author zhanglianyong
     * @date 2022/8/5 19:56
     * @return  地址信息
     **/
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }

    }

    /**
     * 查询指定用户的全部地址
     *
     * @author zhanglianyong
     * @date 2022/8/5 19:58
     * @param addressBook 地址信息
     * @return 用戶的全部地址信息
     **/
    @GetMapping("/list")
    @ApiOperation("查询用户的全部地址信息")
    public R<List<AddressBook>> list(@ApiParam("地址信息") AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(queryWrapper));
    }

}
