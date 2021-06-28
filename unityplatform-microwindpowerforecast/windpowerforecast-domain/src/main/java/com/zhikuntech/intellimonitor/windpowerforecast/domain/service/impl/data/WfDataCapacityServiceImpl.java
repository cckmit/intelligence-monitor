package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCapacity;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataCapacityMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCapacityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 容量 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-18
 */
@Service
public class WfDataCapacityServiceImpl extends ServiceImpl<WfDataCapacityMapper, WfDataCapacity> implements IWfDataCapacityService {

    /**
     * 获取当月全场发电量
     * @param monthBg       yyyy-MM-dd HH:mm:ss
     * @param monthNextBg   yyyy-MM-dd HH:mm:ss
     * @return  全场发电量
     */
    @Override public BigDecimal fetchGenElectricWithDate(String monthBg, String monthNextBg) {
        QueryWrapper<WfDataCapacity> capacityQueryWrapper = new QueryWrapper<>();
        capacityQueryWrapper.gt("event_date_time", monthBg);
        capacityQueryWrapper.lt("event_date_time", monthNextBg);
        capacityQueryWrapper.isNotNull("wind_platform_gen_electric");
        capacityQueryWrapper.orderByDesc("event_date_time");
        List<WfDataCapacity> wfDataCapacities = getBaseMapper().selectList(capacityQueryWrapper);
        if (CollectionUtils.isEmpty(wfDataCapacities)) {
            return null;
        }
        return wfDataCapacities.get(0).getWindPlatformGenElectric();
    }

}
