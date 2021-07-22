package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: intelligence-monitor
 * @Description:分图一右边数据
 * @Author: 沈slk123
 * @CreateDate: 2021/6/17
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanModelDataVO {
    /**
     * 风轮与实时状态
     */
    @ApiModelProperty("风轮")
    @GoldenId(required = true)
    private WindWheelVO windWheeldata;

    /**
     * 齿轮箱
     */
    @ApiModelProperty("齿轮箱")
    @GoldenId(required = true)
    private GearCaseVO geardata;

    /**
     * 发电机与变频箱
     */
    @ApiModelProperty("发电机")
    @GoldenId(required = true)
    private GeneratorVO generatordata;

    /**
     * 轮毂与偏航状态
     */
    @ApiModelProperty("轮毂")
    @GoldenId(required = true)
    private WheelSpiderVO wheelSpiderdata;
}
