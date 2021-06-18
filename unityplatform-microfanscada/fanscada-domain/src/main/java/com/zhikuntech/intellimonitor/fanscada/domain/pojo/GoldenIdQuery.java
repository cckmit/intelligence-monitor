package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author 滕楠
 * @className GoldenIdQuery
 * @create 2021/6/17 11:04
 **/
@Data
public class GoldenIdQuery {

    /**
     * 风机编号
     */
    private List<Integer> dataIds;

}