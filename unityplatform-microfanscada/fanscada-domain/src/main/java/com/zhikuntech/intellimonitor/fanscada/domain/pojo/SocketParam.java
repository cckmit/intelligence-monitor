package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 滕楠
 * @className SocketParam
 * @create 2021/6/25 16:08
 **/
@Data
@ApiModel
public class SocketParam {

    private String username;

    private String sessionId;

    private String message;

    private String messageType;
}