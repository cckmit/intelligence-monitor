package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil.obtainInputStream;

/**
 * @author liukai
 */
public class SftpDataGenerateManipulateUtils {


    static Pattern dqDatePattern = Pattern.compile("\tdate='(.*?)'");


    // 浙江.普陀风电场_72nwp_20210303.rb

    // 浙江.普陀风电场_4Cwind_202103020115.rb

    static SFTPUtil sftpUtil = new SFTPUtil();

    static void readFile() throws IOException {

        // inputStream = obtainInputStream("浙江.普陀风电场_72wind_20210303.rb");

        InputStream inputStream = obtainInputStream("浙江.普陀风电场_4Cwind_202103020115.rb");
        List<String> list = IOUtils.readLines(inputStream, "GBK");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime parse = LocalDateTime.of(LocalDate.parse("2021-07-01", formatter), LocalTime.MIN).plusMinutes(15);
        Month month = parse.getMonth();



        for(;parse.getMonth().equals(month);) {
//            OutputStream outputStream = extractMonth(list, parse);
//            IOUtils.writeLines(list, "\n", outputStream, "GBK");

            LocalDateTime tmp = parse;
            int dayOfMonth = tmp.getDayOfMonth();
            for (;tmp.getDayOfMonth() == dayOfMonth;) {
                upLoadFileCdq(list, tmp);
                tmp = tmp.plusMinutes(15);
            }


            parse = parse.plusDays(1);
        }

    }

    private static void upLoadFileCdq(List<String> list, LocalDateTime parse) throws IOException {
//        changeListDate(list, parse);
        changeListDateCdq(list, parse);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        IOUtils.writeLines(list, "\n", bot, "GBK");
        ByteArrayInputStream bin = new ByteArrayInputStream(bot.toByteArray());
        sftpUtil.upload("wf/cdq",
                "浙江.普陀风电场_4Cwind_" + parse.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".rb",
                bin,
                (long) bin.available());
    }

    private static OutputStream extractMonth(List<String> list, LocalDateTime now) throws IOException {
        changeListDate(list, now);

        // /Users/liukai/Desktop/test_fw

        File file = new File("/Users/liukai/Desktop/test_fw/浙江.普陀风电场_72wind_" +
                now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".rb");
        return new FileOutputStream(file);
    }

    private static void changeListDate(List<String> list, LocalDateTime now) {
        String s = list.get(0);

        Matcher matcher = dqDatePattern.matcher(s);
        boolean b = matcher.find();

        if (b) {
            String group = matcher.group(1);
            String s1 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:15:00";

            s = s.replace(group, s1);
            list.set(0, s);
            System.out.println(s);

//            s.replace()
            System.out.println();
        }
        System.out.println();
    }

    private static void changeListDateCdq(List<String> list, LocalDateTime now) {
        String s = list.get(0);

        Matcher matcher = dqDatePattern.matcher(s);
        boolean b = matcher.find();

        if (b) {
            String group = matcher.group(1);
            String s1 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            s = s.replace(group, s1);
            list.set(0, s);
            System.out.println(s);

//            s.replace()
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {

        readFile();

    }

    static void test0() throws IOException {
        // /Users/liukai/Desktop/test_fw/浙江.普陀风电场_72wind_20210627.rb
        List<String> list = IOUtils.readLines(
                new FileInputStream("/Users/liukai/Desktop/test_fw/浙江.普陀风电场_72wind_20210627.rb"), "GBK"
        );
        Matcher matcher = dqDatePattern.matcher(list.get(0));
//        boolean b = matcher.find();
        System.out.println();
    }

}
