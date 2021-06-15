package com.zhikuntech.intellimonitor.fanscada.domain.utils;

/**
 * @author 肖宇
 * @Since 2021/3/8 下午5:30
 */
/**
 * @description: 使用雪花算法生成全局id
 */
public class SnowflakeIdUtil {
    private static com.zhikuntech.intellimonitor.fanscada.domain.utils.SnowflakeIdWorker idWorker;

    static {
        // 使用静态代码块初始化 SnowflakeIdWorker
        idWorker = new com.zhikuntech.intellimonitor.fanscada.domain.utils.SnowflakeIdWorker(1,1);
    }

    public static Long nextId() {
        return idWorker.nextId();
    }

}