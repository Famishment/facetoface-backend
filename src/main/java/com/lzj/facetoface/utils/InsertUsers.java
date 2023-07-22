package com.lzj.facetoface.utils;

import com.lzj.facetoface.mapper.UserMapper;
import com.lzj.facetoface.model.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Auther: lzj
 * @Date: 2023/7/11-07-11-15:36
 * @Description: com.lzj.facetoface.utils
 */

@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户
     */
    //@Scheduled(fixedDelay = 5000, fixedRate = Long.MAX_VALUE) // 延迟5秒执行
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        System.out.println("gogogogogogogogog");
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户");
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
            //userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
