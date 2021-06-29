package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 代志豪
 * 2021/6/29 18:42
 */
public class TimerUtil {

    private static ConcurrentHashMap<String, Timer> TIMER_MAP = new ConcurrentHashMap<>();

    public static void start(TimerTask task, String username) {
        Timer timer = new Timer(username);
        TIMER_MAP.put(username, timer);
        timer.schedule(task, 20 * 1000);
    }

    public static void stop(String username) {
        Timer timer = TIMER_MAP.get(username);
        timer.cancel();
        TIMER_MAP.remove(username);
    }

}
