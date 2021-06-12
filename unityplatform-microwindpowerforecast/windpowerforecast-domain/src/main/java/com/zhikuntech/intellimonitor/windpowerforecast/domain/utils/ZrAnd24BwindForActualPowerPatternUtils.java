package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.ZrBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.ZrHeaderParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liukai
 */
@Slf4j
public class ZrAnd24BwindForActualPowerPatternUtils {

    /**
     * date表示数据日期
     * 格式:
     * yyyy-MM-dd
     */
    static Pattern zrDatePattern = Pattern.compile("\tdate='(.*?)'");

    /**
     * ampleid表示样本机编号的字符串，以’,’分割
     */
    static Pattern zrSampleIdsPattern = Pattern.compile("\tsampleid='(.*?)'");

    /**
     * samplecap表示样本机装机容量
     */
    static Pattern zrSampleCapPattern = Pattern.compile("\tsamplecap='(.*?)'");

    /**
     * cap表示风场额定装机容量
     */
    static Pattern zrCapPattern = Pattern.compile("\tcap='(.*?)'");


    public static ZrHeaderParse processZrHeader(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        String headRow = strings.get(0);
        // 过滤前后空格
        headRow = StringUtils.trim(headRow);
        if (StringUtils.isBlank(headRow)) {
            return null;
        }
        String zrDate = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(zrDatePattern, headRow));
        String zrSampleIds = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(zrSampleIdsPattern, headRow));
        String zrSampleCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(zrSampleCapPattern, headRow));
        String zrCap = ParseDataFileUtil.fetchStr(ParseDataFileUtil.fetchMatcher(zrCapPattern, headRow));
        ZrHeaderParse zrHeaderParse = ZrHeaderParse.builder()
                .zrDate(StringUtils.trim(zrDate))
                .zrSampleIds(StringUtils.trim(zrSampleIds))
                .zrSampleCap(StringUtils.trim(zrCap))
                .zrSampleCap(StringUtils.trim(zrSampleCap))
                .build();
        if (log.isDebugEnabled()) {
            log.debug("解析zr文件头部:[{}]", zrHeaderParse);
        }
        return zrHeaderParse;
    }

    public static List<ZrBodyParse> processZr(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        int size = strings.size();
        if (size <= 3) {
            return null;
        }
        List<ZrBodyParse> zrBodyParses = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // 只有数据行会被处理
            if (i > 1 && i != (size - 1)) {
                String item = strings.get(i);
                // 过滤前后空格
                item = StringUtils.trim(item);
                /*
                    @顺序	统一编码	时间	实际出力值	开机容量
                 */
                if (StringUtils.isNotBlank(item)) {
                    String[] split = StringUtils.split(item, "\t");
//                    assert split.length == 5;
                    // TODO 转换为实体
                    if (split != null && split.length >= 5) {
                        ZrBodyParse zrBodyParse = ZrBodyParse.builder()
                                .orderNum(split[0])
                                .stationNumber(split[1])
                                .bodyTime(split[2])
                                .actualProduce(split[3])
                                .machineCapacity(split[4])
                                .build();
                        zrBodyParses.add(zrBodyParse);
                    }
                }
            }
        }
        return zrBodyParses;
    }


}
