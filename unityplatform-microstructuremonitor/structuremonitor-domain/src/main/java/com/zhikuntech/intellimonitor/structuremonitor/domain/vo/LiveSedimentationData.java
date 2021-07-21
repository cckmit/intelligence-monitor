package com.zhikuntech.intellimonitor.structuremonitor.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 滕楠
 * @className LiveSedimentationData  沉降数据实体类
 * @create 2021/7/20 17:58
 **/
@ApiModel(description = "沉降数据返回实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveSedimentationData {

    @GoldenId(value = 13)
    @ApiModelProperty("1沉降")
    private Long a1Subside;

    @GoldenId(value = 14)
    @ApiModelProperty("2沉降")
    private Long a2Subside;

    @GoldenId(value = 15)
    @ApiModelProperty("3沉降")
    private Long a3Subside;

    @GoldenId(value = 16)
    @ApiModelProperty("4沉降")
    private Long a4Subside;
}