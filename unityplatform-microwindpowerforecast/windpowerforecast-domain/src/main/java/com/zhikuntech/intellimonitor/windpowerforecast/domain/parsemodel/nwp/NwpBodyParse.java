package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.nwp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NwpBodyParse {

    /**
     * 顺序号
     */
    private String orderNum;

    /**
     * 统一编码
     */
    private String stationNumber;

    /**
     * 数据体中的时间
     */
    private String bodyTime;

    /**
     * 风速
     */
    private String windSpeed;

    /**
     * 高层
     */
    private String highLevel;

    /**
     * 风向
     */
    private String windDirection;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 湿度
     */
    private String humidity;

    /**
     * 气压
     */
    private String pressure;

}
