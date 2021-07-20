package com.zhikuntech.intellimonitor.cable.domain.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author 肖宇
 * @Since 2020/10/9 16:30
 */
@Component
public class RedisLockUtil {



    @Autowired
    private RedisTemplate redisTemplate;


    private static final Long SUCCESS = 1L;
    private long timeout = 5000; //获取锁的超时时间



    /**
     * 加锁，无阻塞
     *
     * @param
     * @param
     * @return
     */
    public Boolean tryLock(String key, long expireTime) {


        Long start = System.currentTimeMillis();
        try{
            for(;;){
                //SET命令返回OK ，则证明获取锁成功
                Boolean ret = redisTemplate.opsForValue().setIfAbsent(key, "lock", expireTime, TimeUnit.SECONDS);
                if(ret){
                    return true;
                }
                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                long end = System.currentTimeMillis() - start;
                if (end>=timeout) {
                    return false;
                }
            }
        }finally {

        }

    }


    /**
     * 解锁
     *
     * @param
     * @param
     * @return
     */
    public Boolean unlock(String key) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);

        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key),"lock");
        if(SUCCESS.equals(result)) {
            return true;
        }

        return false;
    }

}
