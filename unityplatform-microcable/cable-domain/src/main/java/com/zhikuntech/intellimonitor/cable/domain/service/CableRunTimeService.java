package com.zhikuntech.intellimonitor.cable.domain.service;

import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunStressTimeDTO;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunTimeTemperatureDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CableRunTimeService {
    /**
     * 获取当前1号海缆应力实时数据
     */
    List<CableRunStressTimeDTO> getRuntimeStress1() throws Exception;

    /**
     * 获取当前2号海缆应力实时数据
     */
    List<CableRunStressTimeDTO> getRuntimeStress2() throws Exception;

    /**
     * 获取当前3号海缆应力实时数据
     */
    List<CableRunStressTimeDTO> getRuntimeStress3() throws Exception;

    /**
     * 获取当前海缆应力模拟数据
     */
    List<CableRunStressTimeDTO> getRuntimeStressTest() throws Exception;

    /**
     * 获取当前3号海缆温度实时数据
     */
    List<CableRunTimeTemperatureDTO> getRuntimeTemperatureTest() throws Exception;

    /**
     * 获取当前1号海缆温度实时数据
     */
    List<CableRunTimeTemperatureDTO> getRuntimeTemperature1() throws Exception;

    /**
     * 获取当前2号海缆温度实时数据
     */
    List<CableRunTimeTemperatureDTO> getRuntimeTemperature2() throws Exception;

    /**
     * 获取当前3号海缆温度实时数据
     */
    List<CableRunTimeTemperatureDTO> getRuntimeTemperature3() throws Exception;
}
