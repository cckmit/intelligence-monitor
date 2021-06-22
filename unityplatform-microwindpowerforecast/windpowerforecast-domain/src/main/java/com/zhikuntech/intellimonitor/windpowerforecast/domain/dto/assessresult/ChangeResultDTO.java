package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("日考核结果-修改结果")
public class ChangeResultDTO {

    @ApiModelProperty("修改结果[0成功1失败]")
    private Integer result;

    @ApiModelProperty("结果信息")
    private String msg;
}
