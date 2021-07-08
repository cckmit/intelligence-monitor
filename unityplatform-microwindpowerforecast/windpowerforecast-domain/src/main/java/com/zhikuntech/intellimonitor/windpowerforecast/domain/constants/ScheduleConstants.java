package com.zhikuntech.intellimonitor.windpowerforecast.domain.constants;

/**
 * 调度使用属性
 *
 * @author liukai
 */
public class ScheduleConstants {

    //# 补发数据获取

    public final static String FOREST_FETCH_RE_DAY_LOCK = "wf_re_fetch_forest_cur_day";

    public final static String FOREST_FETCH_RE_BEFORE_DAY_LOCK = "wf_re_fetch_forest_before_day";



    //# 获取预测数据锁

    public final static String FOREST_DQ_LOCK = "wf_forest_dq";

    public final static String FOREST_CDQ_LOCK = "wf_forest_cdq";

    public final static String FOREST_NWP_LOCK = "wf_forest_nwp";

    //生成数据的锁
    public final static String FOREST_PARSE_RESULT_LOCK = "wf_forest_parse_result";

    //# 获取预测数据锁

    public final static String GEN_CAP_LOCK = "wf_gen_cap";

    public final static String GEN_POWER_LOCK = "wf_gen_power_lock";

    public final static String GEN_WEATHER_LOCK = "wf_gen_weather_lock";

    // 数据分析调度

    public final static String ANA_DATA_LOCK = "wf_ana_data_lock";

    // 计算漏报次数

    public final static String CALC_HIATUS = "wf_hiatus_data_lock";

    // 评估结果

    public final static String CALC_ASSESS_ELECTRIC = "wf_assess_electric_lock";

}
