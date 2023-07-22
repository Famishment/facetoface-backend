package com.lzj.facetoface.service;

import com.lzj.facetoface.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @Auther: lzj
 * @Date: 2023/7/12-07-12-11:34
 * @Description: com.lzj.facetoface.service
 */

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("yupiString", "dog");
        valueOperations.set("yupiInt", 1);
        valueOperations.set("yupiDouble", 2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("famishment");
        valueOperations.set("yupiUser", user);
        // 查
        Object yupi = valueOperations.get("yupiString");
        Assertions.assertTrue("dog".equals((String)yupi));
        yupi = valueOperations.get("yupiInt");
        Assertions.assertTrue(1 == (Integer) yupi);
        yupi = valueOperations.get("yupiDouble");
        Assertions.assertTrue(2.0 == (Double) yupi);
        System.out.println(valueOperations.get("yupiUser"));
        valueOperations.set("yupiString","dog");
        redisTemplate.delete("yupiString");
    }

}
