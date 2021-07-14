package com.zhikuntech.intellimonitor.structuremonitor.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;

import java.util.List;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 14:06
 */
public interface IStructureMonitoringService {

    /**
     * 结构监测列表查询
     *
     * @param query 查询参数
     * @return : StructureMonitoringQuery
     */
    BaseResponse<List<StructureMonitoringQuery>> getList(StructureMonitoringQuery query);

}
