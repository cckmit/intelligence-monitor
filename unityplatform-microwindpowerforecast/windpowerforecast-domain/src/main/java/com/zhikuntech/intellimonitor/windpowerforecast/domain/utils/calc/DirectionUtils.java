package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 计算方位工具
 *
 * @author liukai
 */
public class DirectionUtils {

    /*
        N
     */

    public static final String DRC_NAME_N_NNE = "n-nne";

    public static final String DRC_NAME_NNE_NE = "nne-ne";

    public static final String DRC_NAME_NE_ENE = "ne-ene";

    public static final String DRC_NAME_ENE_E = "ene-e";


    /*
        E
     */

    public static final String DRC_NAME_E_ESE = "e-ese";

    public static final String DRC_NAME_ESE_SE = "ese-se";

    public static final String DRC_NAME_SE_SSE = "se-sse";

    public static final String DRC_NAME_SSE_S = "sse-s";

    /*
        S
     */

    public static final String DRC_NAME_S_SSW = "s-ssw";

    public static final String DRC_NAME_SSW_SW = "ssw-sw";

    public static final String DRC_NAME_SW_WSW = "sw_wsw";

    public static final String DRC_NAME_WSW_W = "wsw_w";


    /*
        W
     */

    public static final String DRC_NAME_W_WNW = "w-wnw";

    public static final String DRC_NAME_WNW_NW = "wnw-nw";

    public static final String DRC_NAME_NW_NNW = "nw-nnw";

    public static final String DRC_NAME_NNW_N = "nnw_n";


    /**
     * N
     */
    public static BigDecimal drcN = new BigDecimal("0");
    // NNE
    public static BigDecimal drcNNE = new BigDecimal("22.5");
    // NE
    public static BigDecimal drcNE = new BigDecimal("45");
    // ENE
    public static BigDecimal drcENE = new BigDecimal("67.5");

    /**
     * E
     */
    public static BigDecimal drcE = new BigDecimal("90");
    // ESE
    public static BigDecimal drcESE = new BigDecimal("112.5");
    // SE
    public static BigDecimal drcSE = new BigDecimal("135");
    // SSE
    public static BigDecimal drcSSE = new BigDecimal("157.5");

    /**
     * S
     */
    public static BigDecimal drcS = new BigDecimal("180");
    // SSW
    public static BigDecimal drcSSW = new BigDecimal("202.5");
    // SW
    public static BigDecimal drcSW = new BigDecimal("225");
    // WSW
    public static BigDecimal drcWSW = new BigDecimal("247.5");

    /**
     * W
     */
    public static BigDecimal drcW = new BigDecimal("270");
    // WNW
    public static BigDecimal drcWNW = new BigDecimal("292.5");
    // NW
    public static BigDecimal drcNW = new BigDecimal("315");
    // NNW
    public static BigDecimal drcNNW = new BigDecimal("337.5");


    /**
     * 计算风向所在方位
     * @param windDirection 风向
     * @return 风向所在方位范围
     */
    public static String locateDirection(BigDecimal windDirection) {
        /*

            待确定问题,正好落在 N/NNE/NE等上面该如何计算 => (前闭后开)
            待确定夹角问题

            ====>使用前闭合后开的统计方式

            16个方位:
                N
                NNE
                NE
                ENE
                E
                ESE
                SE
                SSE
                S
                SSW
                SW
                WSW
                W
                WNW
                NW
                NNW
         */
        if (Objects.isNull(windDirection)) {
            return null;
        }
        if (windDirection.compareTo(drcN) >= 0 && windDirection.compareTo(drcNNE) < 0) {
            return DRC_NAME_N_NNE;
        } else if (windDirection.compareTo(drcNNE) >= 0 && windDirection.compareTo(drcNE) < 0) {
            return DRC_NAME_NNE_NE;
        } else if (windDirection.compareTo(drcNE) >= 0 && windDirection.compareTo(drcENE) < 0) {
            return DRC_NAME_NE_ENE;
        } else if (windDirection.compareTo(drcENE) >= 0 && windDirection.compareTo(drcE) < 0) {
            return DRC_NAME_ENE_E;
        } else if (windDirection.compareTo(drcE) >= 0 && windDirection.compareTo(drcESE) < 0) {
            return DRC_NAME_E_ESE;
        } else if (windDirection.compareTo(drcESE) >= 0 && windDirection.compareTo(drcSE) < 0) {
            return DRC_NAME_ESE_SE;
        } else if (windDirection.compareTo(drcSE) >= 0 && windDirection.compareTo(drcSSE) < 0) {
            return DRC_NAME_SE_SSE;
        } else if (windDirection.compareTo(drcSSE) >= 0 && windDirection.compareTo(drcS) < 0) {
            return DRC_NAME_SSE_S;
        } else if (windDirection.compareTo(drcS) >= 0 && windDirection.compareTo(drcSSW) < 0) {
            return DRC_NAME_S_SSW;
        } else if (windDirection.compareTo(drcSSW) >= 0 && windDirection.compareTo(drcSW) < 0) {
            return DRC_NAME_SSW_SW;
        } else if (windDirection.compareTo(drcSW) >= 0 && windDirection.compareTo(drcWSW) < 0) {
            return DRC_NAME_SW_WSW;
        } else if (windDirection.compareTo(drcWSW) >= 0 && windDirection.compareTo(drcW) < 0) {
            return DRC_NAME_WSW_W;
        } else if (windDirection.compareTo(drcW) >= 0 && windDirection.compareTo(drcWNW) < 0) {
            return DRC_NAME_W_WNW;
        } else if (windDirection.compareTo(drcWNW) >= 0 && windDirection.compareTo(drcNW) < 0) {
            return DRC_NAME_WNW_NW;
        } else if (windDirection.compareTo(drcNW) >= 0 && windDirection.compareTo(drcNNW) < 0) {
            return DRC_NAME_NW_NNW;
        } else if (windDirection.compareTo(drcNNW) >= 0) {
            return DRC_NAME_NNW_N;
        }
        return null;
    }

}
