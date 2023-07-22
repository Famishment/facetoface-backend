package com.lzj.facetoface.service;

import com.lzj.facetoface.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: lzj
 * @Date: 2022/11/2-11-02-18:20
 * @description: com.lzj.facetoface.service
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();

        user.setUsername("balabala");
        user.setUserAccount("balabala");
        user.setAvatarUrl("https://static.mianshiya.com/logo.png");
        user.setGender(0);
        user.setProfile("山不在高，有仙则灵");
        user.setUserPassword("6aa034476ac13b8d1999ba872307f5d5");
        user.setPhone("123");
        user.setEmail("123@qq.com");
        user.setPlanetCode("10927");

        boolean result = userService.save(user);
        System.out.println(user.getId());

        Assertions.assertTrue(result);
    }

    @Test
    public void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "dogYupi";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
    }

    @Test
    public void testSearchUsersByTags(){
        List<String> tagNameList = Arrays.asList("Java","Python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assert.assertNotNull(userList);
    }
}