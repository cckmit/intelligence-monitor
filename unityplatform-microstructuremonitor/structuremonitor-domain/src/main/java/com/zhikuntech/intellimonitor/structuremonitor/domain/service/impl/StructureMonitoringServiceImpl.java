package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.util.DateUtil;
import com.rtdb.service.impl.HistorianImpl;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.IStructureMonitoringService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtils;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 14:10
 */
@Service
public class StructureMonitoringServiceImpl implements IStructureMonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructureMonitoringServiceImpl.class);

    /**
     * 查询模式 日期
     */
    private final static String DAY = "day";
    /**
     * 查询模式 月份
     */
    private final static String MONTH = "month";
    /**
     * 日期查询拼接后缀
     */
    private final static String START_DAY_SUFFIX = " 00:00:00";
    /**
     * 日期查询拼接后缀
     */
    private final static String END_DAY_SUFFIX = " 23:59:59";
    /**
     * 月份查询拼接后缀
     */
    private final static String MONTH_SUFFIX = "-01";

    @Override
    public BaseResponse<List<StructureMonitoringQuery>> getList(StructureMonitoringQuery query) {
        List<StructureMonitoringQuery> list = new ArrayList<>();
        // 参数校验
        if (Objects.isNull(query)) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "参数不能为空");
        }
        String queryMode = query.getQueryMode();
        if (StringUtils.isBlank(queryMode)) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "参数不能为空");
        }
        // 参数处理
        Date sTime = null;
        Date eTime = null;
        String sStringDate = query.getDateStrPre();
        String eStringDate = query.getDateStrPost();
        LOGGER.info("时间查询字符串参数,[开始时间:{}],[结束时间:{}]", sStringDate, eStringDate);
        if (DAY.equals(queryMode)) {
            sTime = DateUtils.string2Date(sStringDate + START_DAY_SUFFIX, "");
            eTime = DateUtils.string2Date(eStringDate + END_DAY_SUFFIX, "");
        } else if (MONTH.equals(queryMode)) {
            sTime = DateUtils.string2Date(sStringDate + MONTH_SUFFIX + START_DAY_SUFFIX, "");
            LocalDateTime dateTime = DateUtils.string2LocalDateTime(sStringDate + MONTH_SUFFIX, "yyyy-MM-dd");
            // 【sStringDate = 2021-07】=> 【sTime = 2021-07-01 00:00:00】 【eTime = 2021-07-31 23:59:59】
            if (dateTime != null) {
                // 避免异常
                sTime = new Date();
                dateTime = dateTime.with(TemporalAdjusters.lastDayOfMonth());
                eTime = Date.from(LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 23, 59, 59)
                        .atZone(ZoneId.systemDefault()).toInstant());
            }
        }
        if (sTime == null || eTime == null) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "时间参数异常");
        }
        LOGGER.info("时间查询参数转换后,[开始时间:{}],[结束时间:{}]", sTime.toString(), eTime.toString());



        // TODO 数据查询 未完成
        // 从数据库查询查询GoldenId
        // int realCount = his.archivedValuesRealCount(id, sTime, sTime); realCount非0判断 否则抛异常

        return null;
    }

    @Override
    public BaseResponse<LiveData> getData(String type) {

        //todo 获取庚顿id映射关系


        return null;
    }

}
