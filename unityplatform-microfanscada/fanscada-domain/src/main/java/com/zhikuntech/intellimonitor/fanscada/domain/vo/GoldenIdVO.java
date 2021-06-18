package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 滕楠
 * @className GoldenIdQuery
 * @create 2021/6/17 11:04
 **/
@Data
public class GoldenIdVO {

    /**
     * 风机编号
     */
    private Integer windNumber;

    /**
     * 数据库id
     */
    private List<Integer> goldenIds;
}