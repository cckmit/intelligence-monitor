package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Description ${description}
 * @Version 1.0
 */
@Service
public class WinPowerCurveServiceImpl implements WinPowerCurveService {
    GoldenUtil goldenUtil = new GoldenUtil();
    @Override
    public WindPowerCurveVO getWindPowerCurve() throws Exception {
        int[] ids = new int[]{};
        goldenUtil.getSnapshots(ids);  //此处异常统一处理
        WindPowerCurveVO windPowerCurveVO = new WindPowerCurveVO();
        //添加数据
        return windPowerCurveVO;
    }

    @Override
    public boolean subscribeWindPowerCurve(String username) {

        //订阅
//        goldenUtil.subscribeSnapshots();
        return true;
    }
}
