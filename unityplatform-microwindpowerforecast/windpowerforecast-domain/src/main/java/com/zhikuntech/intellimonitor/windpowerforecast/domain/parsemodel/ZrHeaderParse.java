package com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ZrHeaderParse {

    /**
     * yyyy-MM-dd
     */
    private String zrDate;

    private String zrSampleIds;

    private String zrSampleCap;

    private String cap;


    public Date parseZrDateStrToDate() {
        // TODO
        return null;
    }
}
