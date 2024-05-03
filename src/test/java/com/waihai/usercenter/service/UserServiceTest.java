package com.waihai.usercenter.service;

import com.waihai.usercenter.model.request.User;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testSearchUsersByTags(){
        List<String> tagNameList = Arrays.asList("java", "c++");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        for (User user : userList) {
            System.out.println(user.toString());
        }
        Assert.assertNotNull(userList);
    }




}