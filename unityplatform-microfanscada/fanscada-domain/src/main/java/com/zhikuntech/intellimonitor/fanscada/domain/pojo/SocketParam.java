package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import lombok.Data;

/**
 * @author 滕楠
 * @className SocketParam
 * @create 2021/6/25 16:08
 **/
@Data
public class SocketParam {
    private String username;
    private String sessionId;
    private String group;
    private String messageType;
}