package com.waihai.usercenter.service;

import com.waihai.usercenter.model.request.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


@SpringBootTest
public class RedisOptionsTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("waihai", "bird");
        valueOperations.set("waihai1", 2);
        valueOperations.set("waihai2", 2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("waihai");
        user.setUserAccount("haihaihai");
        valueOperations.set("waihai3", user);

        Object objStr = valueOperations.get("waihai");
        Assertions.assertTrue("bird".equals(objStr));
        Object objInt = valueOperations.get("waihai1");
        Assertions.assertTrue(2 == (int) objInt);

        Object objDouble = valueOperations.get("waihai2");
        Assertions.assertTrue(2.0 == (double) objDouble);

        Object objUser = valueOperations.get("waihai3");
        Assertions.assertTrue(user.equals(objUser));


    }
}
