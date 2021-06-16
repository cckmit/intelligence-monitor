package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 18:03
 * @Description ${description}
 * @Version 1.0
 */
@Data
@ApiModel("BackendToGolden查询条件")
public class BackendToGoldenQueryList {
    /**
     * 映射关系中mysql数据库表中编号
     */
    @ApiModelProperty("映射关系中mysql数据库表中编号")
    private List<Integer> backendIdList;

    /**
     * 风机编号
     */
    @ApiModelProperty("风机编号")
    private List<Integer> numberList;
}
