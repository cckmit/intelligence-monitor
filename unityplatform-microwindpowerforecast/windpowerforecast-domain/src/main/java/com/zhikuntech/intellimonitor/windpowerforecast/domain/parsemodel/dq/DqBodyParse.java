package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.dq;

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
public class DqBodyParse {


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
     * 上报出力值
     */
    private String upProduce;

    /**
     * 停机检修容量
     */
    private String stopMachineCheckCapacity;

}
