package com.zhikuntech.intellimonitor.mainpage.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 14:40
 * @Description 时间风速
 * @Version 1.0
 */
@Data
@ApiModel("时间风速")
public class TimeWindSpeedVO {
    /**
     * 时间
     */
    @JSONField(format = "HH:mm")
    @ApiModelProperty("时间")
    private Date date;

    /**
     * 风速
     */
    @ApiModelProperty("风速")
    private Double speedTime;
}
