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
 * 实际功率接收
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
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


}
