package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.cdq;

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
public class CdqHeaderParse {


    /**
     * 日期格式:
     * yyyy-MM-dd HH:mm
     */
    private String cdqDate;

    private String cdqSampleIds;

    private String cdqSampleCap;

    private String cdqCap;

    private String cdqRunningCap;

}
