package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 代志豪
 * 2021/6/29 18:42
 */
public class TimerUtil {

    public static ConcurrentHashMap<String, Timer> TIMER_MAP = new ConcurrentHashMap<>();

    public static void start(TimerTask task, String username) {
        Timer timer = new Timer(username);
        put(username, timer);
        timer.schedule(task, 20 * 1000);
    }

    public static void stop(String username) {
        Timer timer = TIMER_MAP.get(username);
        timer.cancel();
        TIMER_MAP.remove(username);
    }

    private static void put(String username, Timer timer) {
        if (TIMER_MAP.containsKey(username)) {
           TIMER_MAP.get(username).cancel();
        }
        TIMER_MAP.put(username, timer);
    }

}
