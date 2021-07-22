package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.SedimentMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.SedimentData;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.SedimentService;
import org.springframework.stereotype.Service;

/**
 * @author 滕楠
 * @className SedimentServiceImpl
 * @create 2021/7/22 15:30
 **/
@Service
public class SedimentServiceImpl extends ServiceImpl<SedimentMapper, SedimentData> implements SedimentService{
}