package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 滕楠
 * @className FanBaseInfo
 * @create 2021/6/11 17:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "",description = "首页风机尸体返回类")
public class FanBaseInfo {

    @GoldenId(value = 22)

    private BigDecimal windSpeed;
    @GoldenId(value = 24)
    private BigDecimal rotateSpeed;
    @GoldenId(value = 21)
    private BigDecimal activePower;
    @GoldenId(value = 23)
    private BigDecimal reactivePower;
}