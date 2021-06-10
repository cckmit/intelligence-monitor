package com.zhikuntech.intellimonitor.mainpage.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 14:36
 * @Description 时间功率
 * @Version 1.0
 */
@Data
@ApiModel("时间功率")
public class TimePowerVO {
    /**
     * 时间
     */
    @JSONField(format = "HH:mm")
    @ApiModelProperty("时间")
    private Date date;

    /**
     * 功率
     */
    @ApiModelProperty("功率")
    private Double power;
}
