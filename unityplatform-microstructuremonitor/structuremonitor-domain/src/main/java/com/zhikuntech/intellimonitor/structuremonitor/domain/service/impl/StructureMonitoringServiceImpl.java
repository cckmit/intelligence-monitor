package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.constant.StructureConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.constant.DataConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.*;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.*;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSedimentationData;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSpeedData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 14:10
 */
@Service
@Slf4j
public class StructureMonitoringServiceImpl implements IStructureMonitoringService {

    // @Autowired
    // private BackendToGoldenMapper backend;


    @Override
    public BaseResponse<List<StructureMonitoringQuery>> getList(StructureMonitoringQuery query) {
        List<StructureMonitoringQuery> list = new ArrayList<>();
        // 参数校验
        if (Objects.isNull(query)) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "参数不能为空");
        }
        String queryMode = query.getQueryMode();
        String number = query.getNumber();
        if (StringUtils.isBlank(queryMode) || StringUtils.isBlank(number)) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "参数异常");
        }
        // 参数处理
        Date sTime = null;
        Date eTime = null;
        String sStringDate = query.getDateStrPre();
        String eStringDate = query.getDateStrPost();
        log.info("时间查询字符串参数,[开始时间:{}],[结束时间:{}]", sStringDate, eStringDate);
        if (StructureConstant.DAY.equals(queryMode)) {
            sTime = com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil.string2Date(sStringDate + StructureConstant.START_DAY_SUFFIX, "");
            eTime = com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil.string2Date(eStringDate + StructureConstant.END_DAY_SUFFIX, "");
        } else if (StructureConstant.MONTH.equals(queryMode)) {
            // 【sStringDate = 2021-07】=> 【sTime = 2021-07-01 00:00:00】 【eTime = 2021-07-31 23:59:59】
            sTime = com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil.string2Date(sStringDate + StructureConstant.MONTH_SUFFIX, StructureConstant.DATE_PATTERN);
            eTime = DateUtil.getDateByLastDay(sTime);
        }
        if (sTime == null || eTime == null) {
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "时间参数转换异常");
        }
        log.info("时间查询参数转换后,[开始时间:{}],[结束时间:{}]", sTime.toString(), eTime.toString());

        /* TODO 数据查询 */


        return null;
    }

    @Resource
    private StructureToGoldenService structureToGoldenService;

    @Resource
    private StructureSpeedDataMinService minService;

    @Resource
    private StructureSpeedDataMaxService maxService;

    @Resource
    private StructureSpeedDataAvgService avgService;

    /**
     * 加速度数据,一分钟一次,
     *
     * @param type      1:最大值 2:平均值 3:最小值
     * @param fanNumber 风机编号
     * @return
     */
    @Override
    public BaseResponse<LiveSpeedData> getSpeedData(Integer type, Integer fanNumber) {
        // 获取庚顿id映射关系
        if (type.equals(DataConstant.MAX)) {
            List<StructureToGoldenMax> maxMap = structureToGoldenService.getMaxMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = maxMap.stream().mapToInt(StructureToGoldenMax::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationMax(liveSpeedData, snapshots, maxMap);

                //历史数据入库
                if (liveSpeedData != null) {
                    StructureSpeedDataMax max = new StructureSpeedDataMax();
                    max.setDate(liveSpeedData.getDataTime());
                    max.setP1XSpeed(liveSpeedData.getA1xAcceleration().doubleValue());
                    max.setP1YSpeed(liveSpeedData.getA1yAcceleration().doubleValue());
                    max.setP2XSpeed(liveSpeedData.getA2xAcceleration().doubleValue());
                    max.setP2YSpeed(liveSpeedData.getA2yAcceleration().doubleValue());
                    max.setP3XSpeed(liveSpeedData.getA3xAcceleration().doubleValue());
                    max.setP3YSpeed(liveSpeedData.getA3yAcceleration().doubleValue());
                    max.setP4XSpeed(liveSpeedData.getA4xAcceleration().doubleValue());
                    max.setP4YSpeed(liveSpeedData.getA4yAcceleration().doubleValue());
                    max.setP5XSpeed(liveSpeedData.getA5xAcceleration().doubleValue());
                    max.setP5YSpeed(liveSpeedData.getA5yAcceleration().doubleValue());
                    max.setP6XSpeed(liveSpeedData.getA6xAcceleration().doubleValue());
                    max.setP6YSpeed(liveSpeedData.getA6yAcceleration().doubleValue());

                    maxService.save(max);
                }
                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (type.equals(DataConstant.AVG)) {
            List<StructureToGoldenAvg> avgMap = structureToGoldenService.getAvgMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = avgMap.stream().mapToInt(StructureToGoldenAvg::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationAvg(liveSpeedData, snapshots, avgMap);

                //历史数据入库
                if (liveSpeedData != null) {
                    StructureSpeedDataAvg avg = new StructureSpeedDataAvg();
                    avg.setDate(liveSpeedData.getDataTime());
                    avg.setP1XSpeed(liveSpeedData.getA1xAcceleration().doubleValue());
                    avg.setP1YSpeed(liveSpeedData.getA1yAcceleration().doubleValue());
                    avg.setP2XSpeed(liveSpeedData.getA2xAcceleration().doubleValue());
                    avg.setP2YSpeed(liveSpeedData.getA2yAcceleration().doubleValue());
                    avg.setP3XSpeed(liveSpeedData.getA3xAcceleration().doubleValue());
                    avg.setP3YSpeed(liveSpeedData.getA3yAcceleration().doubleValue());
                    avg.setP4XSpeed(liveSpeedData.getA4xAcceleration().doubleValue());
                    avg.setP4YSpeed(liveSpeedData.getA4yAcceleration().doubleValue());
                    avg.setP5XSpeed(liveSpeedData.getA5xAcceleration().doubleValue());
                    avg.setP5YSpeed(liveSpeedData.getA5yAcceleration().doubleValue());
                    avg.setP6XSpeed(liveSpeedData.getA6xAcceleration().doubleValue());
                    avg.setP6YSpeed(liveSpeedData.getA6yAcceleration().doubleValue());

                    avgService.save(avg);
                }
                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type.equals(DataConstant.MIN)) {
            List<StructureToGoldenMin> minMap = structureToGoldenService.getMinMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = minMap.stream().mapToInt(StructureToGoldenMin::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationMin(liveSpeedData, snapshots, minMap);

                //历史数据入库
                if (liveSpeedData != null) {
                    StructureSpeedDataMin min = new StructureSpeedDataMin();
                    min.setDate(liveSpeedData.getDataTime());
                    min.setP1XSpeed(liveSpeedData.getA1xAcceleration().doubleValue());
                    min.setP1YSpeed(liveSpeedData.getA1yAcceleration().doubleValue());
                    min.setP2XSpeed(liveSpeedData.getA2xAcceleration().doubleValue());
                    min.setP2YSpeed(liveSpeedData.getA2yAcceleration().doubleValue());
                    min.setP3XSpeed(liveSpeedData.getA3xAcceleration().doubleValue());
                    min.setP3YSpeed(liveSpeedData.getA3yAcceleration().doubleValue());
                    min.setP4XSpeed(liveSpeedData.getA4xAcceleration().doubleValue());
                    min.setP4YSpeed(liveSpeedData.getA4yAcceleration().doubleValue());
                    min.setP5XSpeed(liveSpeedData.getA5xAcceleration().doubleValue());
                    min.setP5YSpeed(liveSpeedData.getA5yAcceleration().doubleValue());
                    min.setP6XSpeed(liveSpeedData.getA6xAcceleration().doubleValue());
                    min.setP6YSpeed(liveSpeedData.getA6yAcceleration().doubleValue());

                    minService.save(min);
                }

                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return BaseResponse.failure(ResultCode.PARAMETER_ERROR);
    }

    @Resource
    private SedimentService sedimentService;


    @Resource
    private StructureSedimentationDataService structureSedimentationDataService;

    /**
     * 查询沉降数据,一小时一次
     *
     * @param fanNumber 风机编号
     * @return
     */
    @Override
    public BaseResponse<LiveSedimentationData> getSedimentationData(Integer fanNumber) {

        List<StructureToGoldenMin> minMap = structureToGoldenService.getMinMap(fanNumber, DataConstant.SEDIMENTATION_DATA);
        int[] ids = minMap.stream().mapToInt(StructureToGoldenMin::getGoldenId).toArray();
        // 查询庚顿
        try {
            List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
            //数据填充
            LiveSedimentationData liveSedimentationData = new LiveSedimentationData();
            liveSedimentationData = InjectPropertiesUtil.injectByAnnotationMin(liveSedimentationData, snapshots, minMap);
            QueryWrapper<SedimentData> wrapper = new QueryWrapper<>();
            wrapper.eq("fan_number", fanNumber);
            SedimentData one = sedimentService.getOne(wrapper);
            if (liveSedimentationData == null) {
                return BaseResponse.success(null);
            }
            //计算值
            liveSedimentationData.setA1SubsideCalculate(liveSedimentationData.getA1Subside().subtract(BigDecimal.valueOf(one.getValue1())));
            liveSedimentationData.setA2SubsideCalculate(liveSedimentationData.getA2Subside().subtract(BigDecimal.valueOf(one.getValue2())));
            liveSedimentationData.setA3SubsideCalculate(liveSedimentationData.getA3Subside().subtract(BigDecimal.valueOf(one.getValue3())));
            liveSedimentationData.setA4SubsideCalculate(liveSedimentationData.getA4Subside().subtract(BigDecimal.valueOf(one.getValue4())));
            //相对值
            liveSedimentationData.setA2SubsideToa1(liveSedimentationData.getA2Subside().subtract(liveSedimentationData.getA1Subside()));
            liveSedimentationData.setA3SubsideToa1(liveSedimentationData.getA3Subside().subtract(liveSedimentationData.getA1Subside()));
            liveSedimentationData.setA4SubsideToa1(liveSedimentationData.getA4Subside().subtract(liveSedimentationData.getA1Subside()));

            //历史数据存入数据库
            StructureSedimentationData structureSedimentationData = new StructureSedimentationData();
            structureSedimentationData.setDate(liveSedimentationData.getDataTime());
            structureSedimentationData.setP1Calculate(liveSedimentationData.getA1SubsideCalculate().doubleValue());
            structureSedimentationData.setP2Calculate(liveSedimentationData.getA2SubsideCalculate().doubleValue());
            structureSedimentationData.setP3Calculate(liveSedimentationData.getA3SubsideCalculate().doubleValue());
            structureSedimentationData.setP4Calculate(liveSedimentationData.getA4SubsideCalculate().doubleValue());
            structureSedimentationData.setP2ToP1(liveSedimentationData.getA2SubsideToa1().doubleValue());
            structureSedimentationData.setP3ToP1(liveSedimentationData.getA3SubsideToa1().doubleValue());
            structureSedimentationData.setP4ToP1(liveSedimentationData.getA4SubsideToa1().doubleValue());

            structureSedimentationDataService.save(structureSedimentationData);

            return BaseResponse.success(liveSedimentationData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BaseResponse.failure(ResultCode.PARAMETER_ERROR);
    }
}