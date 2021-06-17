package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 实际功率接收
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfDataZr implements Serializable {

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
     * 实际出力值
     */
    private BigDecimal actualProduce;

    /**
     * 开机容量
     */
    private BigDecimal machineCapacity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 样本机编号的字符串，以’,’分割
     */
    private String sampleIds;

    /**
     * 样本机装机容量
     */
    private BigDecimal sampleCap;

    /**
     * 风场额定装机容量
     */
    private BigDecimal cap;

    /**
     * 数据体中的时间信息
     */
    private LocalTime bodyTime;

    private String rowRawData;

    /**
     * 获取状态(0获取成功, 1获取失败)
     */
    private Integer status;

    /**
     * 获取数据的时间
     */
    private LocalDateTime fetchTime;

    /**
     * 失败原因(status为1, 则有此字段有数据说明)
     */
    private String failMsg;


}
