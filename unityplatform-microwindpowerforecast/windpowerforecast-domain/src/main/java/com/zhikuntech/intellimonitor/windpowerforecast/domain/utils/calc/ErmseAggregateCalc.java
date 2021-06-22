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

    BigDecimal dq;

    List<BigDecimal> zrCapsProduce;

    BigDecimal cap;

    public ErmseAggregateCalc() {
        // 初始化数组
        this.dqProduce = new ArrayList<>();
        this.zrCapsProduce = new ArrayList<>();
    }
}
