package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.cdq.CdqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.cdq.CdqHeaderParse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liukai
 */
public class CdqAnd4CWindForSuperShortTimePatternUtils {

    /*
        <WindEnergyPlan::Zhejiang	date='2021-03-02 01:15'	sampleid=''	samplecap='0.00'	cap='252.00'	runningcap='252.00'>
     */

    /**
     * 头部日期
     * 格式:
     * yyyy-MM-dd HH:mm
     */
    static Pattern cdqDatePattern = Pattern.compile("\tdate='(.*?)'");

    static Pattern cdqSampleIdsPattern = Pattern.compile("\tsampleid='(.*?)'");

    static Pattern cdqSampleCapPattern = Pattern.compile("\tsamplecap='(.*?)'");

    static Pattern cdqCapPattern = Pattern.compile("\tcap='(.*?)'");

    static Pattern cdqRunningCapPattern = Pattern.compile("\trunningcap='(.*?)'");

    public static CdqHeaderParse processCdqHeader(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        String headRow = strings.get(0);
        // 过滤前后空格
        headRow = StringUtils.trim(headRow);
        if (StringUtils.isBlank(headRow)) {
            return null;
        }
        String cdqDate = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(cdqDatePattern, headRow));
        String cdqSampleIds = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(cdqSampleIdsPattern, headRow));
        String cdqSampleCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(cdqSampleCapPattern, headRow));
        String cdqCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(cdqCapPattern, headRow));
        String cdqRunningCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(cdqRunningCapPattern, headRow));

        CdqHeaderParse cdqHeaderParse = CdqHeaderParse.builder()
                .cdqDate(cdqDate)
                .cdqSampleIds(cdqSampleIds)
                .cdqSampleCap(cdqSampleCap)
                .cdqCap(cdqCap)
                .cdqRunningCap(cdqRunningCap)
                .build();
        return cdqHeaderParse;
    }

    public static List<CdqBodyParse> processCdqBody(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        int size = strings.size();
        if (size <= 3) {
            return null;
        }
        List<CdqBodyParse> zrBodyParses = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // 只有数据行会被处理
            if (i > 1 && i != (size - 1)) {
                String item = strings.get(i);
                // 过滤前后空格
                item = StringUtils.trim(item);
                /*
                    @顺序	统一编码	时间	上报出力值
                 */
                if (StringUtils.isNotBlank(item)) {
                    String[] split = StringUtils.split(item, "\t");
//                    assert split.length == 5;
                    // TODO 转换为实体
                    if (split != null && split.length >= 4) {
                        CdqBodyParse cdqBodyParse = CdqBodyParse.builder()
                                .orderNum(split[0])
                                .stationNumber(split[1])
                                .bodyTime(split[2])
                                .upProduce(split[3])
                                .build();
                        zrBodyParses.add(cdqBodyParse);
                    }
                }
            }
        }
        return zrBodyParses;
    }

}
