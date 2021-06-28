package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.datafetch;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.nwp.NwpBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.nwp.NwpHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liukai
 */
public class NwpAnd72nwpWeatherPatternUtils {


    /*
        <WindEnergyNwp::Zhejiang	date='2021-03-03 00:15:00'	Coordinates='E23.234200,N10.232300'	TurbinH='70'>
     */

    /**
     * 头部日期
     * 格式:
     * yyyy-MM-dd HH:mm:ss
     */
    static Pattern nwpDatePattern = Pattern.compile("\tdate='(.*?)'");

    static Pattern nwpCoordinatesPattern = Pattern.compile("\tCoordinates='(.*?)'");

    static Pattern nwpTurbinHPattern = Pattern.compile("\tTurbinH='(.*?)'");


    public static NwpHeaderParse parseNwpHeader(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        String headRow = strings.get(0);
        // 过滤前后空格
        headRow = StringUtils.trim(headRow);
        if (StringUtils.isBlank(headRow)) {
            return null;
        }
        String nwpDate = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(nwpDatePattern, headRow));
        String nwpCoordinates = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(nwpCoordinatesPattern, headRow));
        String nwpTurbinH = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(nwpTurbinHPattern, headRow));

        NwpHeaderParse nwpHeaderParse = NwpHeaderParse.builder()
                .nwpDate(nwpDate)
                .nwpCoordinates(nwpCoordinates)
                .nwpTurbinH(nwpTurbinH)
                .build();
        return nwpHeaderParse;
    }

    public static List<NwpBodyParse> parseNwpBody(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        int size = strings.size();
        if (size <= 3) {
            return null;
        }
        List<NwpBodyParse> nwpBodyParses = new ArrayList<>();
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
                        // @顺序	统一编码	时间	风速	高层	风向	温度	湿度	气压
                        NwpBodyParse nwpBodyParse = NwpBodyParse.builder()
                                .orderNum(split[0])
                                .stationNumber(split[1])
                                .bodyTime(split[2])
                                .windSpeed(split[3])
                                .highLevel(split[4])
                                .windDirection(split[5])
                                .temperature(split[6])
                                .humidity(split[7])
                                .pressure(split[8])
                                .build();

                        nwpBodyParses.add(nwpBodyParse);
                    }
                }
            }
        }
        return nwpBodyParses;
    }

}
