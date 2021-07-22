package com.zhikuntech.intellimonitor.structuremonitor.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 滕楠
 * @className StructureSedimentationData
 * @create 2021/7/22 17:49
 **/
@Data
@TableName("structure_sedimentation_data")
public class StructureSedimentationData {


    private Date date;
    //计算值
    private Double p1Calculate;
    private Double p2Calculate;
    private Double p3Calculate;
    private Double p4Calculate;
    //相对值
    private Double p2ToP1;
    private Double p3ToP1;
    private Double p4ToP1;
}