package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liukai
 */
@Data
public class ErmseAggregateCalc {

    LocalDateTime eventTime;

    /**
     * 短期只会有一条数据
     */
    List<BigDecimal> dqProduce;

    /**
     * 短期/超短期预测
     */
    BigDecimal foreset;

    List<BigDecimal> zrCapsProduce;

    /**
     * 计算短期/超短期时使用
     */
    BigDecimal cap;

    /**
     * 计算考核结果时使用的容量
     */
    BigDecimal capForAssess;

    public ErmseAggregateCalc() {
        // 初始化数组
        this.dqProduce = new ArrayList<>();
        this.zrCapsProduce = new ArrayList<>();
    }
}
