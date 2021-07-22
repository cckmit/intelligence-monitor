package com.zhikuntech.intellimonitor.structuremonitor.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 沉降初始值
 * @author 滕楠
 * @className SedimentData
 * @create 2021/7/22 11:31
 **/
@TableName("sedimentation_initial_data")
@Data
public class SedimentData {

    private Integer fanNumber;

    private Double value1;

    private Double value2;

    private Double value3;

    private Double value4;
}