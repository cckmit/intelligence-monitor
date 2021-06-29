package com.zhikuntech.intellimonitor.mainpage.domain.service;

import com.zhikuntech.intellimonitor.mainpage.domain.dto.WindPowerCurveDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import org.springframework.stereotype.Service;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Version 1.0
 */
@Service
public interface WinPowerCurveService {
    /**
     * 获取所有时间内的【风功率曲线】数据
     * @return
     */
    WindPowerCurveDTO getWindPowerCurveOfAllTime(NwpCurvePatternQuery nwpCurvePatternQuery);
}
