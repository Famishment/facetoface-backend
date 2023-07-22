package com.lzj.facetoface.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.facetoface.model.domain.User;
import com.lzj.facetoface.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热定时任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    // 重点用户
    private List<Long> mainUserList = Arrays.asList(8L);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 45 19 * * *")
    public void doCacheRecommendUsers() {
        // 获取锁的对象
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock：" + Thread.currentThread().getId());
                for (Long userId: mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20), queryWrapper);
                    String redisKey = String.format("yupao:user:recommend:%s",userId);
                    redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        redisTemplate.opsForValue().set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS); // 30s,过期
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUsers error", e);
        } finally {
            // 如果放到try里面，一旦报错，将不会执行释放锁的操作。
            // 释放锁（只能释放自己的锁，不能释放别人的锁），此方法根据线程的id来识别
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock" + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
