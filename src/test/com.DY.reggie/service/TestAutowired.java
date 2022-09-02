package service;

import com.DY.reggie.ReggieApplication;
import com.DY.reggie.dto.DishDto;
import com.DY.reggie.entity.Dish;
import com.DY.reggie.entity.User;
import com.DY.reggie.service.DishService;
import com.DY.reggie.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @author zhanglianyong
 * 2022/9/215:13
 */
@SpringBootTest(classes = ReggieApplication.class)
@RunWith(SpringRunner.class)
public class TestAutowired {

    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;

    @Test
    public void test() {
        User user = userService.getById(1552823152492638210L);
        System.out.println(user.toString());
    }

    @Test
    public void test1() {
        DishDto dishDto = dishService.getByIdWithFlavor(1397849739276890114L);
        System.out.println(dishDto.toString());
    }

}
