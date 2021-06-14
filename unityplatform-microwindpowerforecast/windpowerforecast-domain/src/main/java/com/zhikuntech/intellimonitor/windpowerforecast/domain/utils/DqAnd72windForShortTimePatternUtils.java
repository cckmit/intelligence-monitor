package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.DqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.DqHeaderParse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liukai
 */
public class DqAnd72windForShortTimePatternUtils {

    /*
        D.第一行标签行（<WindEnergyPlan::Zhejiang）中date表示数据起始时刻、
        sampleid表示样本机编号的字符串，以’,’分割，samplecap表示样本机装机容量，
        cap表示风场额定装机容量；
     */

    /**
     * 头部日期
     * 格式:
     * yyyy-MM-dd HH:mm
     */
    static Pattern dqDatePattern = Pattern.compile("\tdate='(.*?)'");

    static Pattern dqSampleIdsPattern = Pattern.compile("\tsampleid='(.*?)'");

    static Pattern dqSampleCapPattern = Pattern.compile("\tsamplecap='(.*?)'");

    static Pattern dqCapPattern = Pattern.compile("\tcap='(.*?)'");


    public static DqHeaderParse processDqHeader(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        String headRow = strings.get(0);
        // 过滤前后空格
        headRow = StringUtils.trim(headRow);
        if (StringUtils.isBlank(headRow)) {
            return null;
        }
        String dqDate = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(dqDatePattern, headRow));
        String dqSampleIds = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(dqSampleIdsPattern, headRow));
        String dqSampleCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(dqSampleCapPattern, headRow));
        String dqCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(dqCapPattern, headRow));

        DqHeaderParse dqHeaderParse = DqHeaderParse.builder()
                .dqDate(dqDate)
                .dqSampleIds(dqSampleIds)
                .dqSampleCap(dqSampleCap)
                .dqCap(dqCap)
                .build();
        return dqHeaderParse;
    }

    public static List<DqBodyParse> processDqBody(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        int size = strings.size();
        if (size <= 3) {
            return null;
        }
        List<DqBodyParse> dqBodyParses = new ArrayList<>();
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
                        DqBodyParse dqBodyParse = DqBodyParse.builder()
                                .orderNum(split[0])
                                .stationNumber(split[1])
                                .bodyTime(split[2])
                                .upProduce(split[3])
                                .stopMachineCheckCapacity(split[4])
                                .build();

                        dqBodyParses.add(dqBodyParse);
                    }
                }
            }
        }

        return dqBodyParses;
    }

}
