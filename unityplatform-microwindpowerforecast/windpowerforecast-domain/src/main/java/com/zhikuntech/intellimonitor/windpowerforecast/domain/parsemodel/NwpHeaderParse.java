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
public class NwpHeaderParse {

    /**
     * 日期格式:
     * yyyy-MM-dd HH:mm:ss
     */
    private String nwpDate;

    private String nwpCoordinates;

    private String nwpTurbinH;
}
