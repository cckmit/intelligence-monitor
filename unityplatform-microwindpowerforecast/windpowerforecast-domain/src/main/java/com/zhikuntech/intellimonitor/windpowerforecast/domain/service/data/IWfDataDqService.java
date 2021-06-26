package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.DqDayElectricGenDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataDq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 短期功率预测 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
public interface IWfDataDqService extends IService<WfDataDq> {

    /**
     * 日发电量预测计算
     * @param date 日期格式[yyyy-MM-dd]
     * @return 日发电量数组
     */
    List<DqDayElectricGenDTO> dayElectricGen(String date);

    void batchSave();
}
