package com.zhikuntech.intellimonitor.core.stream.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 0 -> 正常
 * 1 -> 异常
 *
 * <p>
 *  遥测数据(number)/遥信(bool)
 *  单个测点保持时序.
 * </p>
 *
 * 测点数据传输结构
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MonitorStructDTO implements Serializable {

    private static final long serialVersionUID = 4712986913026329713L;

    /**
     * 唯一编码
     */
    private String uuid;

    /**
     * 测点编码
     */
    private String monitorNo;

    /**
     * 测点值
     */
    private BigDecimal monitorValue;

    /**
     * 测点值 - 字符串值
     */
    private String monitorValueStr;

    /**
     * 事件时间戳 - 毫秒戳
     */
    private Long eventTimeStamp;
}
