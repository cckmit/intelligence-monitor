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
@TableName("structure_speed_data_avg")
public class StructureSpeedDataAvg {

    private Date date;

    private Double p1XSpeed;

    private Double p1YSpeed;

    private Double p2XSpeed;

    private Double p2YSpeed;

    private Double p3XSpeed;

    private Double p3YSpeed;

    private Double p4XSpeed;

    private Double p4YSpeed;

    private Double p5XSpeed;

    private Double p5YSpeed;

    private Double p6XSpeed;

    private Double p6YSpeed;
}