package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.constant.StructureConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.constant.DataConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenAvg;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMax;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.IStructureMonitoringService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSedimentationData;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSpeedData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        if (type == 1) {
            List<StructureToGoldenMax> maxMap = structureToGoldenService.getMaxMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = maxMap.stream().mapToInt(StructureToGoldenMax::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationMax(liveSpeedData, snapshots, maxMap);

                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == 2) {
            List<StructureToGoldenAvg> avgMap = structureToGoldenService.getAvgMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = avgMap.stream().mapToInt(StructureToGoldenAvg::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationAvg(liveSpeedData, snapshots, avgMap);

                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == 3) {
            List<StructureToGoldenMin> minMap = structureToGoldenService.getMinMap(fanNumber, DataConstant.SPEED_DATA);
            int[] ids = minMap.stream().mapToInt(StructureToGoldenMin::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSpeedData liveSpeedData = new LiveSpeedData();
                liveSpeedData = InjectPropertiesUtil.injectByAnnotationMin(liveSpeedData, snapshots, minMap);

                return BaseResponse.success(liveSpeedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return BaseResponse.failure(ResultCode.PARAMETER_ERROR);
    }

    /**
     * 查询沉降数据,一小时一次
     *
     * @param type      1:最大值 2:平均值 3:最小值
     * @param fanNumber 风机编号
     * @return
     */
    @Override
    public BaseResponse<LiveSedimentationData> getSedimentationData(Integer type, Integer fanNumber) {
        if (type == 1) {
            List<StructureToGoldenMax> maxMap = structureToGoldenService.getMaxMap(fanNumber, DataConstant.SEDIMENTATION_DATA);
            int[] ids = maxMap.stream().mapToInt(StructureToGoldenMax::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSedimentationData liveSedimentationData = new LiveSedimentationData();
                liveSedimentationData = InjectPropertiesUtil.injectByAnnotationMax(liveSedimentationData, snapshots, maxMap);

                return BaseResponse.success(liveSedimentationData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == 2) {
            List<StructureToGoldenAvg> avgMap = structureToGoldenService.getAvgMap(fanNumber, DataConstant.SEDIMENTATION_DATA);
            int[] ids = avgMap.stream().mapToInt(StructureToGoldenAvg::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSedimentationData liveSedimentationData = new LiveSedimentationData();
                liveSedimentationData = InjectPropertiesUtil.injectByAnnotationAvg(liveSedimentationData, snapshots, avgMap);

                return BaseResponse.success(liveSedimentationData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == 3) {
            List<StructureToGoldenMin> minMap = structureToGoldenService.getMinMap(fanNumber, DataConstant.SEDIMENTATION_DATA);
            int[] ids = minMap.stream().mapToInt(StructureToGoldenMin::getGoldenId).toArray();
            // 查询庚顿
            try {
                List<ValueData> snapshots = GoldenUtil.getSnapshots(ids);
                LiveSedimentationData liveSedimentationData = new LiveSedimentationData();
                liveSedimentationData = InjectPropertiesUtil.injectByAnnotationMin(liveSedimentationData, snapshots, minMap);

                return BaseResponse.success(liveSedimentationData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return BaseResponse.failure(ResultCode.PARAMETER_ERROR);
    }
}