package com.zhikuntech.intellimonitor.core.commons.constant;

/**
 * @author 代志豪
 * 2021/6/25 10:09
 * <p>
 * websocket订阅消息常量
 */
public interface WebSocketConstant {

    /**
     * 首页风机列表实时消息
     */
    String MAIN_PAGE_RUNTIME = "mainPageRuntime";

    /**
     * 首页风场统计实时消息
     */
    String MAIN_PAGE_STATISTICS = "mainPageStatistics";

    /**
     * 海上在线监测实时消息
     */
    String ONLINE_MONITOR_RUNTIME_SEA = "onlineMonitorRuntimeSea";

    /**
     * 陆上在线监测实时消息
     */
    String ONLINE_MONITOR_RUNTIME_LAND = "onlineMonitorRuntimeLand";

    /**
     * 海上在线监测曲线图消息
     */
    String ONLINE_MONITOR_GRAPH_SEA = "onlineMonitorGraphSea";

    /**
     * 陆上在线监测曲线图消息
     */
    String ONLINE_MONITOR_GRAPH_LAND = "onlineMonitorGraphLand";

    /**
     * 订阅(取消)所有
     */
    String ALL = "all";

    /**
     * 后端推送返回通配符
     */
    String PATTERN = "->";
}
