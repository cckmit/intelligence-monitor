package com.zhikuntech.intellimonitor.fanscada.domain.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 存放string类型
     *
     * @param key     key
     * @param data    数据
     * @param timeout 超时间
     */
    public void setString(String key, String data, Long timeout) {
        stringRedisTemplate.opsForValue().set(key, data);
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public <T> T getEntity(String key, Class<T> t) {
        String json = getString(key);
        return JSONObject.parseObject(json, t);
    }

    public void putEntity(String key, Object object) {
        String json = JSONObject.toJSONString(object);
        setString(key, json);
    }

    /**
     * 获取一个redis分布锁
     *
     * @param lockKey        锁住的key
     * @param lockExpireMils 锁住的时长。如果超时未解锁，视为加锁线程死亡，其他线程可夺取锁
     * @param timeOut        超时时间
     * @return
     */
    public String lock(String lockKey, long lockExpireMils, long timeOut) {
        return (String) redisTemplate.execute((RedisCallback) connection -> {
            long endTime = System.currentTimeMillis() + lockExpireMils + 1;
            while (System.currentTimeMillis() < endTime) {
                String lockValue = UUID.randomUUID().toString();
                // 当多个不同的jvm同时创建一个相同的rediskey 只要谁能够创建成功谁就能够获取锁
                Boolean flag = connection.setNX(lockKey.getBytes(), lockValue.getBytes());
                if (flag) {
                    // 加上有效期
                    connection.expire(lockKey.getBytes(), timeOut / 1000);
                    return lockValue;
                    // 退出循环
                }
            }
            return null;
        });
    }


    /**
     * 分布式锁 释放锁 （必须是自己创建的分布式锁）
     *
     * @param lockKey
     * @param lockValue
     * @return
     */
    public boolean unlock(String lockKey, String lockValue) {
        return (boolean) redisTemplate.execute((RedisCallback) connection -> {
            byte[] oldValue = connection.get(lockKey.getBytes());
            if (oldValue != null && lockValue.equals(new String(oldValue))) {
                Long result = connection.del(lockKey.getBytes());
                return result != null && result.intValue() == 1;
            }
            return Boolean.TRUE;
        });
    }

    /**
     * 存放string类型
     *
     * @param key  key
     * @param data 数据
     */
    public void setString(String key, String data) {
        setString(key, data, null);
    }

    /**
     * 根据key查询string类型
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 根据对应的key删除key
     */
    public void delKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除
     */
    public <K> void delKeys(Collection<K> keys) {
        redisTemplate.delete(keys);
    }


    public <S, T> void setMap(String key, Map<S, T> map, Long timeout) {
        stringRedisTemplate.opsForHash().putAll(key, map);
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public <S, T> Map<S, T> getMap(String key) {
        return (Map<S, T>) stringRedisTemplate.opsForHash().entries(key);
    }


    public <K> Set<K> getFuzzy(String key) {
        return redisTemplate.keys(key);
    }


    /**
     * 获取键值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置键值
     *
     * @param key
     * @param value
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 指定缓存失效时间
     *
     * @param key
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取List的内容
     *
     * @param key
     * @param start 开始索引
     * @param end   结束索引 0 到 -1代表所有值
     * @return
     */
    public List<?> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list的长度
     *
     * @param key
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */
    public boolean lSets(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSets(String key, List<?> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List同时指定过期时间
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */
    public boolean lSets(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lRightPor(String key) {
        try {
            redisTemplate.opsForList().rightPop(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//	/**
//	 * 加锁
//	 * @param lockKey 加锁的Key
//	 * @param timeStamp 时间戳：当前时间+超时时间
//	 * @return
//	 */
//	public boolean lock(String lockKey,String timeStamp){
//		if(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, timeStamp)){
//			// 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
//			return true;
//		}
//
//		//设置失败，获得锁失败
//		// 判断锁超时 - 防止原来的操作异常，没有运行解锁操作 ，防止死锁
//		String currentLock = stringRedisTemplate.opsForValue().get(lockKey);
//		// 如果锁过期 currentLock不为空且小于当前时间
//		if(!StringUtils.isEmpty(currentLock) && Long.parseLong(currentLock) < System.currentTimeMillis()){
//			//如果lockKey对应的锁已经存在，获取上一次设置的时间戳之后并重置lockKey对应的锁的时间戳
//			String preLock = stringRedisTemplate.opsForValue().getAndSet(lockKey, timeStamp);
//
//			//假设两个线程同时进来这里，因为key被占用了，而且锁过期了。
//			//获取的值currentLock=A(get取的旧的值肯定是一样的),两个线程的timeStamp都是B,key都是K.锁时间已经过期了。
//			//而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的timeStamp已经变成了B。
//			//只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
//			if(!StringUtils.isEmpty(preLock) && preLock.equals(currentLock)){
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	/**
//	 * 释放锁
//	 * @param lockKey
//	 * @param timeStamp
//	 */
//	public void release(String lockKey,String timeStamp){
//		try {
//			String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
//			if(!StringUtils.isEmpty(currentValue) && currentValue.equals(timeStamp) ){
//				// 删除锁状态
//				stringRedisTemplate.opsForValue().getOperations().delete(lockKey);
//			}
//		} catch (Exception e) {
//			System.out.println("警报！警报！警报！解锁异常");
//		}
//	}

    /**
     * 模糊删除
     *
     * @param key
     */
    public void batchDel(String key) {
        if (!StringUtils.isEmpty(key)) {
            Set<String> keys = getFuzzy(key);
            if (!CollectionUtils.isEmpty(keys)) {
                delKeys(keys);
            }
        }
    }

}
