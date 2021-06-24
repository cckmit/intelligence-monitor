package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 风机参数
 *
 * @author： DAI
 * @date： Created in 2021/6/17 10:46
 */
@Data
@TableName(value = "fs_basic_parameter")
public class FsBasicParameterVO {

    private Integer id;
    /**
     * 风机厂家编号
     */
    @ApiModelProperty("风机厂家编号")
    private String number;

    /**
     * 风机厂家名称
     */
    @ApiModelProperty("风机厂家名称")
    private String manufacturer;

    /**
     * 风机型号
     */
    @ApiModelProperty("风机型号")
    private String model;

    /**
     * 轮毂高度
     */
    @ApiModelProperty("轮毂高度")
    private String hubHeight;

    /**
     * 切入风速
     */
    @ApiModelProperty("切入风速")
    private String inWindSpeed;

    /**
     * 切出风速
     */
    @ApiModelProperty("切出风速")
    private String outWindSpeed;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
