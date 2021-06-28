package com.zhikuntech.intellimonitor.core.commons.dto;

import lombok.Data;

/**
 * @Author 杨锦程
 * @Date 2021/6/23 19:59
 * @Description 请求头参数
 * @Version 1.0
 */
@Data
public class HeaderParamDTO {
    private String applicationToken;
    private String xxlSsoSessionid;
}
