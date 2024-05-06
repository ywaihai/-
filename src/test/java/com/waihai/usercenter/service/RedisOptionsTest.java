package com.waihai.usercenter.service;

import com.waihai.usercenter.model.request.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


@SpringBootTest
@Slf4j
public class RedisOptionsTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test() {
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

    @Test
    public void test1() {
//        RList<Integer> rList = redissonClient.getList("test-list");
//        rList.add(2);
//        rList.add(3);
//
//        System.out.println("rList: " + rList.get(0) + " " + rList.get(1));

        RMap<String, Integer> rMap = redissonClient.getMap("test-map");
        rMap.put("waihai1", 1);
        rMap.put("waihai2", 2);
        System.out.println("rMap: " + rMap.get("waihai1") + " " + rMap.get("waihai2"));

    }

    @Test
    public void testWatchDog(){
        RLock rlock = redissonClient.getLock("waihai:user:center:lock");

        try {
            if (rlock.tryLock(0, 30000, TimeUnit.MILLISECONDS)) {
                System.out.println("Locked: " + Thread.currentThread().getId());
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            log.error("lock set error: ", e.getMessage());
        } finally {
            if (rlock.isHeldByCurrentThread()) {
                System.out.println("Unlock: " + Thread.currentThread().getId());
            }
            rlock.unlock();
        }

    }
}
