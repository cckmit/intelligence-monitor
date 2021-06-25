package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import lombok.Data;

/**
 * @author 代志豪
 * 2021/6/25 9:44
 */
@Data
public class WebSocketTransferVo {

    /**
     * webscoket 连接用户名
     */
    private String username;

    /**
     * 指令:订阅，取消订阅，重置
     */
    private Integer orderType;

    /**
     * 指令详情
     */
    private String description;

}
