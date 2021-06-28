package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 14:40
 * @Description 时间风速
 * @Version 1.0
 */
@Data
public class TimeWindSpeedDTO {
    /**
     * 时间
     */
    @JsonFormat(pattern = "HH:mm",timezone = "GMT+8")
    private LocalDateTime date;

    /**
     * 风速
     */
    private BigDecimal speedTime;
}
