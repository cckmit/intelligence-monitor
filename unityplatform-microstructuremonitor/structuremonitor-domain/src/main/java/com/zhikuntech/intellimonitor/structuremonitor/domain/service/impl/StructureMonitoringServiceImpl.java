package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.constant.StructureConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.IStructureMonitoringService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.DateUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveData;
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

    @Override
    public BaseResponse<LiveData> getData(String type, Integer fanNumber) {
        // 获取庚顿id映射关系
        List<StructureToGolden> map = structureToGoldenService.getMap(fanNumber);
        int[] ids = map.stream().mapToInt(StructureToGolden::getGoldenId).toArray();
        // 查询庚顿
        List<ValueData> snapshots = null;
        try {
            snapshots = GoldenUtil.getSnapshots(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StructureToGolden structureToGolden = new StructureToGolden();
        StructureToGolden structureToGolden1 = InjectPropertiesUtil.injectByAnnotation(structureToGolden, snapshots, map);

        return null;
    }

}
