package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

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
//

    public static void main(String[] args) throws IOException {

        // 实际功率     zr      24Bwind     浙江.红山风电_24Bwind_20170321.rb
        InputStream inputStream = obtainInputStream("浙江.红山风电_24Bwind_20170321.rb");
        List<String> strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));
        /*
            <WindEnergyPlan::Zhejiang	date='2017-03-21'	sampleid='1#,13#,23#'	samplecap='1.50'	cap='48.00'>
            @顺序	统一编码	时间	实际出力值	开机容量
            #1	浙江.红山风电P	00:15:00	0.000	48.0
            </WindEnergyPlan::Zhejiang>
         */


        // 短期功率     dq      72wind


        // 超短期功率    cdq     4Cwind


        // 天气预报     nwp     72nwp       暂无


        // 实测气象     cft     cft

        System.out.println();
    }

    private static InputStream obtainInputStream(String fileName) throws FileNotFoundException {
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
        String filePath = path + "sampleparse/";
        log.info("filePath is [{}]. resourcePath is [{}]", filePath, path);
        return filePath;
    }

}
