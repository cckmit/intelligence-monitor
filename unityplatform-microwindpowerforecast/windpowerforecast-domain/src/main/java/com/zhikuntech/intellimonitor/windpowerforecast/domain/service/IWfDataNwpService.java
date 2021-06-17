package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.DqDayElectricGenDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataNwp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.NwpListPatternQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 数值天气预报 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
public interface IWfDataNwpService extends IService<WfDataNwp> {

    /**
     * 曲线展示-列表查询
     * @param query 查询
     * @return  预测数据数组
     */
    Pager<NwpListPatternDTO> nwpListQuery(NwpListPatternQuery query);

    /**
     * 曲线展示-曲线查询
     * @param query 查询
     * @return 预测数据数组
     */
    List<NwpListPatternDTO> nwpCurveQuery(NwpListPatternQuery query);



    /**
     * 测试:
     * 批量查询数据
     * @return 批量获取数据
     */
    List<WfDataNwp> queryBatch();

    /**
     * 查询气象预测高度
     * @return 气象预测高度结果
     */
    List<BigDecimal> queryHigh();

    /**
     * 批量保存数据
     */
    void batchSave();
}
