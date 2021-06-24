package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author liukai
 */
public class AssessCalcUtils {


    public static BigDecimal calcAssessRatioR1(List<ErmseAggregateCalc> aggrs) {
        BigDecimal allAggre = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCapForAssess();
            // ppk
            BigDecimal ppk = new BigDecimal("0");
            for (BigDecimal dqProduce : dqProduces) {
                ppk = ppk.add(dqProduce);
            }
            ppk = ppk.divide(BigDecimal.valueOf(dqProduces.size()), 3, RoundingMode.HALF_UP);
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);

            // [(pmk - ppk) / ck] ^ 2
            BigDecimal kkk = pmk.subtract(ppk).divide(ck, 3, RoundingMode.HALF_EVEN)
                    .pow(2);
            allAggre = allAggre.add(kkk);
        }
        // sqrt(r/n)
        double v = allAggre.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN).doubleValue();
        v = Math.sqrt(v);
        BigDecimal resV = new BigDecimal(v);
        // (1 - resV) * 100%
        return new BigDecimal("1").subtract(resV, new MathContext(3, RoundingMode.HALF_EVEN)).multiply(new BigDecimal("100"));
    }
}
