package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 代志豪
 * @date 2021-06-07
 */

@Data
@Accessors(chain = true)
public class WarningDto {

    private Integer id;

    /**
     * 告警时间
     */
    private LocalDateTime datetime;

    /**
     * 错误代码
     */
    private String warnCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 告警等级
     */
    private Integer warnLevel;

    /**
     * 设备id
     */
    private Integer equipmentId;

    /**
     * 设备类型
     */
    private String equipmentType;


}
