package com.zhikuntech.intellimonitor.fanscada.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 代志豪
 * 2021/6/9 18:27
 * mysql数据库和golden数据库关于点位的映射关系
 */
@Data
@ApiModel("mysql数据库和golden数据库关于点位的映射关系")
@TableName("mp_backen_to_golden")
public class BackendToGolden {

    /**
     * 表id
     */
    @ApiModelProperty("表id")
    @TableField("id")
    private int id;

    /**
     * 映射关系中mysql数据库表中编号
     */
    @ApiModelProperty("映射关系中mysql数据库表中编号")
    @TableField("backendId")
    private int backendId;

    /**
     * 映射关系中golden数据库表中id
     */
    @ApiModelProperty("映射关系中golden数据库表中id")
    @TableField("goldenId")
    private int goldenId;

    /**
     * 风机编号
     */
    @ApiModelProperty("风机编号")
    @TableField("number")
    private int number;

    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    @TableField("description")
    private String description;

    /**
     * 标签点名称
     */
    @ApiModelProperty("标签点名称")
    @TableField("tagName")
    private String tagName;

}
