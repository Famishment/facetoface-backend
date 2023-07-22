package com.lzj.facetoface.service;

import com.lzj.facetoface.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: lzj
 * @Date: 2023/7/11-07-11-15:59
 * @Description: com.lzj.facetoface.service
 */
@SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserService userService;
    // CPU 密集型：分配的核心线程数 = CPU - 1
    // IO 密集型：分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000));

    /**
     * 批量插入用户
     */
    @Test
    //@Scheduled(fixedDelay = 5000, fixedRate = Long.MAX_VALUE) // 延迟5秒执行
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假鱼皮");
            user.setUserAccount("fakeyupi");
            user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.gcloud.la/img/logo.png");
            user.setGender(0);
            user.setProfile("在水一方");
            user.setUserPassword("12345678");
            user.setPhone("18755667123");
            user.setEmail("555@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("111");
            user.setTags("[]");
            userList.add(user);
        }
        // 6秒 1000条数据，一次100条
        // 20秒 100000条数据，一次1000条    18秒 10000条一次   17秒 50000条一次
        userService.saveBatch(userList,10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 并发批量插入用户
     */
    @Test
    public void doConcurrencyInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        int j = 0;
        int batchSize = 5000;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假用户");
                user.setUserAccount("faker");
                user.setAvatarUrl("https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500");
                user.setGender(0);
                user.setProfile("在水一方");
                user.setUserPassword("12345678");
                user.setPhone("18755667123");
                user.setEmail("555@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("101");
                user.setTags("[]");
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 此方法是异步执行的
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("ThreadName：" + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            },executorService);
            futureList.add(future);
        }
        // 用join阻塞一下
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
