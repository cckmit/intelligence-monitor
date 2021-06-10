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
 * 超短期功率预测
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WfDataCdq implements Serializable {

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
    private Integer orderNum;

    /**
     * 统一编码
     */
    private String stationNumber;

    /**
     * 事件事件
     */
    private LocalDateTime eventDateTime;

    /**
     * 上报出力值
     */
    private BigDecimal forecastProduce;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 本机编号的字符串，以’,’分割
     */
    private String sampleIds;

    /**
     * 本机装机容量
     */
    private BigDecimal sampleCap;

    /**
     * 风场额定装机容量
     */
    private BigDecimal cap;

    /**
     * 风场实时开机容量
     */
    private BigDecimal runningCap;


}
