package com.zhikuntech.intellimonitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CableApplication.class})
public class CableApplicationTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @Test public void redisManipulate() {

    }

}
