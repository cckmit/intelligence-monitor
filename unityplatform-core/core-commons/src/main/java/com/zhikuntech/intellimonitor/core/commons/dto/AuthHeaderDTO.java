package com.zhikuntech.intellimonitor.core.commons.dto;

import lombok.Data;

/**
 * @Author 杨锦程
 * @Date 2021/6/24 10:07
 * @Description 传参使用
 * @Version 1.0
 */
@Data
public class AuthHeaderDTO {
    private AuthDTO authDTO;
    private HeaderParamDTO headerParamDTO;
}
