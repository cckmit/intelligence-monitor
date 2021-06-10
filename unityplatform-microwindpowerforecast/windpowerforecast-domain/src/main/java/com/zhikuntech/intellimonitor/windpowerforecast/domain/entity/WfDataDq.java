package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短期功率预测
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WfDataDq implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 顺序
     */
    private Integer orderNum;

    /**
     * 统一编码
     */
    private String stationNumber;

    /**
     * 事件生成事件
     */
    private LocalDateTime eventDateTime;

    /**
     * 上报出力值
     */
    private BigDecimal forecastProduce;

    /**
     * 上报检修预测
     */
    private BigDecimal forecastCheck;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
