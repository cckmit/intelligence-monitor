package com.zhikuntech.intellimonitor.core.commons.constant;

/**
 * @author： DAI
 * @date： Created in 2021/7/15 15:44
 */
public interface StructureConstant {
    /**
     * 查询模式 日期
     */
    String DAY = "day";
    /**
     * 查询模式 月份
     */
    String MONTH = "month";
    /**
     * 日期查询拼接后缀
     */
    String START_DAY_SUFFIX = " 00:00:00";
    /**
     * 日期查询拼接后缀
     */
    String END_DAY_SUFFIX = " 23:59:59";
    /**
     * 月份查询拼接后缀
     */
    String MONTH_SUFFIX = "-01";
    /**
     * 日期格式化
     */
    String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式化
     */
    String DATE_PATTERN = "yyyy-MM-dd";
}
