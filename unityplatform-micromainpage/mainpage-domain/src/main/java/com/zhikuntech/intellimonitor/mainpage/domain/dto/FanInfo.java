package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 代志豪
 * @date 2021-06-07
 */

@Data
@Accessors(chain = true)
public class FanInfo {

    private Integer id;

    /**
     * 风机编号
     */
    private Integer number;

    /**
     * 风机厂家
     */
    private String manufacturer;

    /**
     * 风机类型
     */
    private String tpye;

    /**
     * 轮毂高度
     */
    private Double hubHeight;

    /**
     * 切入风速
     */
    private Double speedIn;

    /**
     * 切出风速
     */
    private Double speedOut;


}
