package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import java.math.BigDecimal;

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




}
