package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件解析结果
 * </p>
 *
 * @author liukai
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WfBasicParseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 版本(乐观锁)
     */
    private Integer version;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * dq|cdq|nwp(短期预测|超短期预测|气象预测)
     */
    private String fileType;

    private Integer successMark;

    /**
     * 解析失败原因
     */
    private String failReason;

    /**
     * 文件二进制数据
     */
    private Blob fileData;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件前缀
     */
    private String filePrefix;

    /**
     * 风场名称
     */
    private String windFarmName;


}
