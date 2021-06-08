package com.zhikuntech.intellimonitor.mainpage.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cyd
 * @title: BaseEntity
 * @projectName monitor-parent
 * @description: 实体基类，审计字段
 * @date 2020/4/2318:08
 */
@Data
public class BaseEntity {

    private Long id;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime createTime;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 修改者
     */
    private String modifier;
}
