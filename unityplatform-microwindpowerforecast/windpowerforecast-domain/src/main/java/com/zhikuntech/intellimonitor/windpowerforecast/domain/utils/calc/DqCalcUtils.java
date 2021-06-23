package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author liukai
 */
@Slf4j
public class DqCalcUtils {


    public static BigDecimal calcErmse(List<ErmseAggregateCalc> aggrs) {
        BigDecimal allAggre = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
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
        return new BigDecimal(v, new MathContext(3, RoundingMode.HALF_EVEN));
    }

    public static BigDecimal calcEMAE(List<ErmseAggregateCalc> aggrs) {
        BigDecimal emae = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
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

            // |pmk - ppk| / ck
            BigDecimal kkk = pmk.subtract(ppk).abs().divide(ck, 3, RoundingMode.HALF_EVEN);
            emae = emae.add(kkk);
        }
        // r / n
        emae = emae.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN);
        return emae;
    }

    public static BigDecimal calcMaxe(List<ErmseAggregateCalc> aggrs) {
        BigDecimal maxe = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ppk
            BigDecimal ppk = aggr.getForeset();
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            // max(pmk - ppk)
            BigDecimal mSk = pmk.subtract(ppk);
            maxe = maxe.compareTo(mSk) > 0 ? maxe : mSk;
        }
        return maxe;
    }

    public static BigDecimal calcAboutR(List<ErmseAggregateCalc> aggrs) {
        BigDecimal pm = new BigDecimal("0");
        BigDecimal pp = new BigDecimal("0");

        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();
            // ppk
            BigDecimal ppk = aggr.getForeset();

            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);

            pm = pm.add(pmk);
            pp = pp.add(pp);
        }
        pm = pm.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN);
        pp = pp.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN);

        BigDecimal molecule = new BigDecimal("0");
        BigDecimal demoninatorPre = new BigDecimal("0");
        BigDecimal demoninatorPost = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ppk
            BigDecimal ppk = aggr.getForeset();
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);

            // (pmk - pm)*(ppk - pm)
            BigDecimal kmkm = pmk.subtract(pm).multiply(ppk.subtract(pp)).setScale(3, RoundingMode.HALF_EVEN);
            molecule = molecule.add(kmkm);

            // (pmk - pm)^2
            BigDecimal km2 = pmk.subtract(pm).pow(2);
            demoninatorPre = demoninatorPre.add(km2);
            // (ppk - pm)^2
            BigDecimal kp2 = ppk.subtract(pp).pow(2);
            demoninatorPost = demoninatorPost.add(kp2);
        }
        // molecule / sqrt(demoninatorPre * demoninatorPost)
        double sqrt = Math.sqrt(demoninatorPre.multiply(demoninatorPost).doubleValue());
        return molecule.divide(new BigDecimal(sqrt), 3, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcAboutR2(List<ErmseAggregateCalc> aggrs) {
        BigDecimal ratioR2 = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
            // ppk
            BigDecimal ppk = aggr.getForeset();
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }

            // 1 - ( |pmk - ppk| / ck )
            BigDecimal kkk = pmk.subtract(ppk).abs().divide(ck, 3, RoundingMode.HALF_EVEN);
            kkk = new BigDecimal("1").subtract(kkk);
            if (kkk.compareTo(new BigDecimal("0.75")) >= 0) {
                ratioR2 = ratioR2.add(new BigDecimal("1"));
            } else {
                ratioR2 = ratioR2.add(new BigDecimal("0"));
            }
        }
        ratioR2 = ratioR2.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
        return ratioR2;
    }
}
