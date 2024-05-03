package com.waihai.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waihai.usercenter.model.request.User;
import com.waihai.usercenter.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    // 只对重要数据进行缓存
    private List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 20 15 * * *") // 测试时间自行修改
    public void doCacheRecommendUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> userIPage = userService.page(new Page<>(1, 8), queryWrapper);
        String redisKey = String.format("waihai:user:center:%s", mainUserList);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        try {
            valueOperations.set(redisKey, userIPage, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis key set error", e);
        }
    }
}
