package com.zhikuntech.intellimonitor.structuremonitor.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 滕楠
 * @className StructureToGolden
 * @create 2021/7/19 10:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mp_structure_to_golden_max")
public class StructureToGoldenMax {

    private Integer id;

    private Integer goldenId;

    private Integer backenId;

    private Integer fanNumber;

    private Integer dataType;
}