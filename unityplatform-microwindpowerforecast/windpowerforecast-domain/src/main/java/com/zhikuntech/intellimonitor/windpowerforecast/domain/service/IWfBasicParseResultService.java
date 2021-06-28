package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfBasicParseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

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
