package com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfTimeBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 时间基准表 Mapper 接口
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
public interface WfTimeBaseMapper extends BaseMapper<WfTimeBase> {


    /**
     * 分组查询
     *
     * @param page      页码分页
     * @param datePre   日期前
     * @param datePost  日期后
     * @param timeRatio 时间跨度
     * @return 分组后的结果
     */
    List<NwpListPatternDTO> nwpListPattern(Page<NwpListPatternDTO> page,
                                           @Param("datePre") String datePre,
                                           @Param("datePost") String datePost,
                                           @Param("timeRatio") Integer timeRatio,
                                           @Param("nwp_high") Integer nwpHigh,
                                           @Param("cf_high") Integer cfHigh);


    /**
     * 全量查询
     *
     * @param datePre   日期前
     * @param datePost  日期后
     * @param timeRatio 时间跨度
     * @return 全量查询
     */
    List<NwpListPatternDTO> nwpCurvePattern(@Param("datePre") String datePre,
                                           @Param("datePost") String datePost,
                                           @Param("timeRatio") Integer timeRatio,
                                            @Param("nwp_high") Integer nwpHigh,
                                            @Param("cf_high") Integer cfHigh);
}
