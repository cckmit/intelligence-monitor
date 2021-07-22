package com.zhikuntech.intellimonitor.structuremonitor.domain.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureSpeedDataAvg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author tn
 */
@Mapper
@Repository
public interface StructureSpeedDataAvgMapper extends BaseMapper<StructureSpeedDataAvg> {
}
