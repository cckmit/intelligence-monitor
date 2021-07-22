package com.zhikuntech.intellimonitor.structuremonitor.domain.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureSpeedData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author tn
 */
@Mapper
@Repository
public interface StructureSpeedDataMapper extends BaseMapper<StructureSpeedData> {
}
