package com.zhikuntech.intellimonitor.mainpage.domain.service;

import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import org.springframework.stereotype.Service;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Description ${description}
 * @Version 1.0
 */
@Service
public interface WinPowerCurveService {
    /**
     * 获取庚顿数据库中当前【风功率曲线】数据
     * @return
     */
    WindPowerCurveVO getWindPowerCurve() throws Exception;

    /**
     * 订阅【风功率曲线】相关标签点快照改变的通知
     * @return 订阅结果
     */
    boolean subscribeWindPowerCurve(String username);
}