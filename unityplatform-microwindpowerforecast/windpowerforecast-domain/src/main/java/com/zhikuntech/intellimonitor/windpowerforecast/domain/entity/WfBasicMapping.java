package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 编码映射
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfBasicMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 风场名称
     */
    private String stationName;

    /**
     * 风场编码
     */
    private String stationNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
