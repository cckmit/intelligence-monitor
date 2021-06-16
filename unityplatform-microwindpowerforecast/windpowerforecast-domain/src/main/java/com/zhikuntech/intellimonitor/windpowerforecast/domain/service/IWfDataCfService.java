package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCf;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfListPatternQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 实测气象 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-15
 */
public interface IWfDataCfService extends IService<WfDataCf> {

    /**
     * 查询高度
     * @return 高度列表
     */
    List<BigDecimal> queryHigh();

    /**
     * 查询实测风列表数据
     * @param query 查询条件
     * @return  分页的列表
     */
    Pager<CfListDTO> cfListQuery(CfListPatternQuery query);
}
