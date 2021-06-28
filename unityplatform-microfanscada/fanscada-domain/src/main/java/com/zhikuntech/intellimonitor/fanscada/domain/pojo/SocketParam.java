package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 滕楠
 * @className SocketParam
 * @create 2021/6/25 16:08
 **/
@Data
@ApiModel
public class SocketParam {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("session")
    private String sessionId;
    @ApiModelProperty("消息内容")
    private String message;
    @ApiModelProperty("订阅模式,默认01")
    private String messageType;
}