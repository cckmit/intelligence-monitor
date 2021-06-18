package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: intelligence-monitor
 * @Description:分图一右边数据
 * @Author: 沈slk123
 * @CreateDate: 2021/6/17
 * @Version:
 */
@Data
public class FanModelDataVO {
    /**
     * 风轮与实时状态
     */
    @ApiModelProperty("风轮")
    private WindWheelVO windWheeldata;

    /**
     * 齿轮箱
     */
    @ApiModelProperty("齿轮箱")
    private GearCaseVO geardata ;

    /**
     * 发电机与变频箱
     */
    @ApiModelProperty("发电机")
    private GeneratorVO generatordata;

    /**
     * 轮毂与偏航状态
     */
    @ApiModelProperty("轮毂")
    private WheelSpiderVO wheelSpiderdata;
}
