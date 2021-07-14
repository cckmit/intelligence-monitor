package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfBasicParseResult;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.ZtMonitorQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文件解析结果 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-28
 */
public interface IWfBasicParseResultService extends IService<WfBasicParseResult> {

    /**
     * 补发今日之前数据
     */
    void relaunchDayBefore();

    /**
     * 补发今日未处理数据
     */
    void reLaunchCurDayPreLoss();

    /**
     * 根据时间和文件类型获取sftp文件
     * @param dateTime  时间
     * @param type      文件类型(dq|cdq|nwp)
     */
    void fetchDqWithPointDate(LocalDateTime dateTime, /*dq|cdq|nwp*/ String type);

    /**
     * 根据时间查三种文件的状态 0-已读取入库|2-未读取
     * @param  query  要查询的时间
     * @return int[] 结果 三个数 超短期 短期 天气 1已读取 |0未读取
     */
    List<Integer> stateJudge(ZtMonitorQuery query);

    /**
     * 根据类型和时间查三种文件的状态 0-已读取入库|2-未读取
     * @param dateTime  时间
     * @param type      类型 文件类型(dq|cdq|nwp)
     * @return int结果  1已读取 |0未读取
     */

    int fileStatusQuery(LocalDateTime dateTime,String type);

    /**
     * 根据时间判断数据库有没有文件名表的数据
     * @param dateTime  时间
     * @return  int    结果1在 0不在
     */
    int judge(LocalDateTime dateTime);

    /**
     * 生成短期数据
     * @param localDate 数据日期
     */
    void genDqDataNeedFetch(LocalDate localDate);

    /**
     * 生成超短期数据
     * @param localDate 数据日期
     */
    void genCdqDataNeedFetch(LocalDate localDate);

    /**
     * 生成气象预测数据
     * @param localDate 数据日期
     */
    void genNwpDataNeedFetch(LocalDate localDate);

}
