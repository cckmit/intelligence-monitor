package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 时间基准表
 * </p>
 *
 * @author liukai
 * @since 2021-06-17
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfTimeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  (1/5/15单位min)
     */
    private Integer timeRatio;

    private LocalDateTime dateTime;

    private String dateTimeStr;


}
