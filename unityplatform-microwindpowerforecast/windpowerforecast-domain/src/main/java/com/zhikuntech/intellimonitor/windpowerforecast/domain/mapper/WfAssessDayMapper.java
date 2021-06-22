package com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.DayAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日考核结果 Mapper 接口
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
public interface WfAssessDayMapper extends BaseMapper<WfAssessDay> {

    /**
     * 查询列表模式
     *
     * @param pager 分页参数
     * @param mPre 日期前
     * @param mPost 日期后
     * @return 分页后的结果
     */
    List<DayAssessListDTO> dayListPattern(Page<DayAssessListDTO> pager, @Param("mPre") String mPre, @Param("mPost") String mPost);

}
