package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 文件解析结果
 * </p>
 *
 * @author liukai
 * @since 2021-06-28
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    /**
     * 0成功1失败2未解析
     */
    private Integer successMark;

    /**
     * 解析失败原因
     */
    private String failReason;

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

    private LocalDateTime dataGenDate;


}
