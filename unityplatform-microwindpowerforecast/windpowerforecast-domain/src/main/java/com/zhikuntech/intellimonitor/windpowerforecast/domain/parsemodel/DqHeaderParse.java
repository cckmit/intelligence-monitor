package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DqHeaderParse {


    /**
     * 日期格式:
     * yyyy-MM-dd HH:mm
     */
    private String dqDate;

    private String dqSampleIds;

    private String dqSampleCap;

    private String dqCap;

}
