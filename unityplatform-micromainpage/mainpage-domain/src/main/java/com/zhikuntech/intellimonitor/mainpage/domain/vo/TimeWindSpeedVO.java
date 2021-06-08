package com.zhikuntech.intellimonitor.mainpage.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 14:40
 * @Description 时间风速
 * @Version 1.0
 */
@Data
public class TimeWindSpeedVO {
    /**
     * 时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date date;

    /**
     * 风速
     */
    private Double speedTime;
}
