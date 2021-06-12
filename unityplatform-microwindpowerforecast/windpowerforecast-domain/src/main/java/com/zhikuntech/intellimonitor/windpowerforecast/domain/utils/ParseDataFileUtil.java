package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.ZrBodyParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件编码使用GBK
 * <p>
 *     原文:
 *     注：E文本中涉及的所有中文字符均必须采用GBK编码方式。
 * </p>
 *
 * @author liukai
 */
@Slf4j
public class ParseDataFileUtil {


    public static void main(String[] args) throws IOException {


        InputStream inputStream = null;
        List<String> strings = null;

//        // 实际功率     zr      24Bwind     浙江.红山风电_24Bwind_20170321.rb
//        inputStream = obtainInputStream("浙江.红山风电_24Bwind_20170321.rb");
//        strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));
//        /*
//            <WindEnergyPlan::Zhejiang	date='2017-03-21'	sampleid='1#,13#,23#'	samplecap='1.50'	cap='48.00'>
//            @顺序	统一编码	时间	实际出力值	开机容量
//            #1	浙江.红山风电P	00:15:00	0.000	48.0
//            </WindEnergyPlan::Zhejiang>
//         */
//        ZrAnd24BwindForActualPowerPatternUtils.processZr(strings);




        // 短期功率     dq      72wind      浙江.红山风电_72wind_20170323.rb
        inputStream = obtainInputStream("浙江.普陀风电场_72wind_20210303.rb");
        strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));


        // 超短期功率    cdq     4Cwind      浙江.红山风电_4Cwind_201703221700.rb
        inputStream = obtainInputStream("浙江.普陀风电场_4Cwind_202103020115.rb");
        strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

        CdqHeaderParse cdqHeaderParse = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqHeader(strings);
        List<CdqBodyParse> cdqBodyParses = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqBody(strings);
        System.out.println();


        // 天气预报     nwp     72nwp       暂无
        inputStream = obtainInputStream("浙江.普陀风电场_72nwp_20210303.rb");
        strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));


        // 实测气象     cft     cft         浙江.红山风电_cft_201703221700.rb
        inputStream = obtainInputStream("浙江.普陀风电场_cft_202103020100.rb");
        strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

        System.out.println();
    }

    static void processMultipleMark() {
        // TODO
    }


    static Matcher fetchMatcher(Pattern p, String str) {
        return p.matcher(str);
    }

    static String fetchStr(Matcher m) {
        if (Objects.isNull(m)) {
            return null;
        }
        boolean b = m.find();
        if (b) {
            return m.group(1);
        }
        return null;
    }

    public static InputStream obtainInputStream(String fileName) throws FileNotFoundException {
        String fuZr = obtainFullPath(fileName);
        File file = Paths.get(fuZr).toFile();
        return new FileInputStream(file);
    }

    public static String obtainFullPath(String fileName) {
        return findFilePath() + fileName;
    }

    private static String findFilePath() {
        URL resource = ParseDataFileUtil.class.getResource("/");
        String path = resource.getPath();
        // /Users/liukai/business/intelligence-monitor/unityplatform-microwindpowerforecast/windpowerforecast-domain/src/main/resources/actualenv
        path = "/Users/liukai/business/intelligence-monitor/unityplatform-microwindpowerforecast/windpowerforecast-domain/src/main/resources/";
        String filePath = path + "actualenv/";
        log.info("filePath is [{}]. resourcePath is [{}]", filePath, path);
        return filePath;
    }

}
