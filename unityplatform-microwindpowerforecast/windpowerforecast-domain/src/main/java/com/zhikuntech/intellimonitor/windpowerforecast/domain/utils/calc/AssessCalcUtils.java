package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author liukai
 */
public class AssessCalcUtils {


    /**
     * 计算准确率
     */
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

    /**
     * 短期功率预测准确率考核电量
     * @param dqRatioR1 准确率
     * @return 考核电量
     */
    public static BigDecimal calcDqElectricR1(BigDecimal dqRatioR1) {
        return new BigDecimal("0.80").subtract(dqRatioR1)
                    .multiply(new BigDecimal("252")).multiply(new BigDecimal("0.2"))
                    .multiply(new BigDecimal("1")).setScale(3, RoundingMode.HALF_EVEN);
    }

    /**
     * 短期功率预测准确率考核费用
     * @param dqElectricR1  短期考核电量
     * @return  短期考核费用
     */
    public static BigDecimal calcDqPayR1(BigDecimal dqElectricR1) {
        return dqElectricR1.multiply(new BigDecimal("1000"))
                    .multiply(new BigDecimal("0.4153"))
                    .setScale(3, RoundingMode.HALF_EVEN);
    }

    /**
     * 超短期功率预测准确率考核电量
     * @param cdqRatioR1    准确率
     * @return  考核电量
     */
    public static BigDecimal calcCdqElectricR1(BigDecimal cdqRatioR1) {
        return new BigDecimal("0.85").subtract(cdqRatioR1)
                    .multiply(new BigDecimal("252")).multiply(new BigDecimal("0.2"))
                    .multiply(new BigDecimal("1")).setScale(3, RoundingMode.HALF_EVEN);
    }

    /**
     * 超短期功率预测准确率考核费用
     * @param cdqElectricR1 考核电量
     * @return  考核费用
     */
    public static BigDecimal calcCdqPayR1(BigDecimal cdqElectricR1) {
        return cdqElectricR1.multiply(new BigDecimal("1000"))
                    .multiply(new BigDecimal("0.4153"))
                    .setScale(3, RoundingMode.HALF_EVEN);
    }
}
