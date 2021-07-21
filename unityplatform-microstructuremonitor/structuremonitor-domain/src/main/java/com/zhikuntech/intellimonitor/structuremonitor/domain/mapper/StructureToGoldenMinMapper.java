package com.zhikuntech.intellimonitor.structuremonitor.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 滕楠
 * @className StructureToGoldenMapper
 * @create 2021/7/19 10:49
 **/
@Mapper
@Repository
public interface StructureToGoldenMinMapper extends BaseMapper<StructureToGoldenMin> {
}