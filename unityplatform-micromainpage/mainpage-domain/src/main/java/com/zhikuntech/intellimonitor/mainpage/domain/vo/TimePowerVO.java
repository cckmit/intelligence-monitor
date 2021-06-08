package com.zhikuntech.intellimonitor.mainpage.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 14:36
 * @Description 时间功率
 * @Version 1.0
 */
@Data
public class TimePowerVO {
    /**
     * 时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date date;

    /**
     * 功率
     */
    private Double power;
}
