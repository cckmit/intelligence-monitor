package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess;

import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.ChangeResultDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessMonth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessUpdateQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthCurveQuery;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 月考核结果 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
public interface IWfAssessMonthService extends IService<WfAssessMonth> {


    /**
     * 更新月考核数据
     * @param query 待更新数据
     * @return  更新结果
     */
    ChangeResultDTO monthAssessUpdate(MonthAssessUpdateQuery query);

    /**
     * 月考核结果-列表模式
     *
     * @param query 根据年进行查询
     * @return  分页结果
     */
    Pager<MonthAssessListDTO> queryMonthList(MonthAssessQuery query);


    /**
     * 月考核结果-曲线模式
     *
     * @param query 根据年进行查询
     * @return  曲线结果
     */
    List<MonthAssessCurveDTO> queryMonthCurve(MonthCurveQuery query);

}
