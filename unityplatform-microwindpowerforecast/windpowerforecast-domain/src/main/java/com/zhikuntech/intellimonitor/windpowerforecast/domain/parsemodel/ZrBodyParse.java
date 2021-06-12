package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel;

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
public class ZrBodyParse {


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
     * 实际出力值
     */
    private String actualProduce;

    /**
     * 开机容量
     */
    private String machineCapacity;
}
