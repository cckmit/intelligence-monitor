package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预测天气列表
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("曲线展示-列表模式")
public class NwpListPatternDTO {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("日期时间")
    private LocalDateTime dateTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日期")
    private LocalDateTime date;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "HH:mm")
    @ApiModelProperty("时间")
    private LocalDateTime time;

    @ApiModelProperty("短期预测功率（MW）")
    private BigDecimal dqPower;

    @ApiModelProperty("超短期预测功率（MW）")
    private BigDecimal cdqPower;

    @ApiModelProperty("实际功率（MW）")
    private BigDecimal actPower;

    //# 预测天气    (风速/气压/温度/湿度/风向)

    @ApiModelProperty("预测-风速")
    private BigDecimal nwpWindSpeed;

    @ApiModelProperty("预测-气压")
    private BigDecimal nwpPressure;

    @ApiModelProperty("预测-温度")
    private BigDecimal nwpTemperature;

    @ApiModelProperty("预测-湿度")
    private BigDecimal nwpHumidity;

    @ApiModelProperty("预测-风向")
    private BigDecimal nwpWindDirection;

    //# 预测天气


    //# 实测天气    (风速/气压/温度/湿度/风向)

    @ApiModelProperty("实测-风速")
    private BigDecimal cfWindSpeed;

    @ApiModelProperty("实测-气压")
    private BigDecimal cfPressure;

    @ApiModelProperty("实测-温度")
    private BigDecimal cfTemperature;

    @ApiModelProperty("实测-湿度")
    private BigDecimal cfHumidity;

    @ApiModelProperty("实测-风向")
    private BigDecimal cfWindDirection;

    //# 实测天气

}
