package com.zhikuntech.intellimonitor.structuremonitor.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.SedimentData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author 滕楠
 * @className SedimentMapper
 * @create 2021/7/22 11:33
 **/
@Mapper
@Repository
public interface SedimentMapper extends BaseMapper<SedimentData> {

}