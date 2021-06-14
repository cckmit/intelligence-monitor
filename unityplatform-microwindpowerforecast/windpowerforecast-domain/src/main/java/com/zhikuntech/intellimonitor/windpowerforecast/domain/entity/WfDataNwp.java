package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 数值天气预报
 * </p>
 *
 * @author liukai
 * @since 2021-06-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfDataNwp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 顺序号
     */
    private String orderNum;

    /**
     * 统一编码
     */
    private String stationNumber;

    /**
     * 数据体数据,数值表示
     */
    private Integer bodyTime;

    /**
     * 事件时间,根据数据体时间计算得出
     */
    private LocalDateTime eventDateTime;

    /**
     * 风速
     */
    private BigDecimal windSpeed;

    /**
     * 高层
     */
    private BigDecimal highLevel;

    /**
     * 风向
     */
    private BigDecimal windDirection;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * 湿度
     */
    private BigDecimal humidity;

    /**
     * 气压
     */
    private BigDecimal pressure;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 天气预报数据点的经纬度坐标
     */
    private String coordinates;

    /**
     * 风场风机轮毂高度
     */
    private BigDecimal turbineHigh;

    /**
     * 头部时间
     */
    private LocalDateTime headerDate;


}
