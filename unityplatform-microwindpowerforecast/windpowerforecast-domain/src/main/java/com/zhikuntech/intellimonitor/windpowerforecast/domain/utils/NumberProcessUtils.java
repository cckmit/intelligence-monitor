package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author liukai
 */
public class NumberProcessUtils {


    public static BigDecimal strToBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            str = StringUtils.trim(str);
            return new BigDecimal(str);
        } catch (Exception ex) {
            // parse exception
            assert ex instanceof NumberFormatException;
            // 简单记录异常
            ex.printStackTrace();
            return null;
        }
    }

}
