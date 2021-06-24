package com.zhikuntech.intellimonitor.windpowerforecast.facade;

import com.zhikuntech.intellimonitor.windpowerforecast.prototype.dto.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author liukai
 */
@FeignClient(name = "wind-power-forecast-app", path = "/normal-usage-play")
public interface ForeCastCurveFacade {

    /**
     * 曲线展示-曲线模式查询
     * @param query 查询
     * @return  曲线模式数据
     */
    @PostMapping("/query-nwp-curve")
    List<NwpListPatternDTO> nwpCurveQuery(@RequestBody NwpCurvePatternQuery query);
}
