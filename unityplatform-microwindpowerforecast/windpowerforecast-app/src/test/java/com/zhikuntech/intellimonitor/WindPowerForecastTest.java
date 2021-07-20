package com.zhikuntech.intellimonitor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfTimeBaseMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfCurvePatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfListPatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseCdqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseDqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessMonthService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.cdqcalc.CdqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc.DqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ConstantsOfWf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.CalcCommonUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WindPowerForecastApplication.class})
public class WindPowerForecastTest {

    @Autowired
    private IWfDataZrService iWfDataZrService;

    @Autowired
    private IWfDataCdqService iWfDataCdqService;

    @Autowired
    private IWfDataDqService dqService;

    @Autowired
    private IWfDataNwpService nwpService;

    @Autowired
    private ScheduleFetchDataService scheduleFetchDataService;

    @Test public void
    test0() {
        WfDataZr wfDataZr = WfDataZr.builder()
                .createTime(LocalDateTime.now())
                .orgId("111")
                .build();
        boolean save = iWfDataZrService.save(wfDataZr);
        assert save;


        ScheduledFuture<?> future;

    }

    @Test public void
    saveBatch() {
        iWfDataCdqService.batchSave();
        System.out.println();
    }

    @Test public void
    testDqSaveBatch() {
        dqService.batchSave();
        System.out.println();
    }

    @Test public void
    testNwpSaveBatch() {
        nwpService.batchSave();
        System.out.println();
    }

    //# query
//    @Test public void
//    testQueryBatch() {
//        List<WfDataNwp> wfDataNwps = nwpService.queryBatch();
//        System.out.println(wfDataNwps);
//        System.out.println();
//    }

    //# invoke remote interface
    @Test public void
    invokeRemote() {
        System.out.println();
        scheduleFetchDataService.scheduleFetchActPower();
        scheduleFetchDataService.scheduleFetchActWeather();

    }

    @Autowired
    private IWfDataCfService iWfDataCfService;

    @Test public void
    queryHigh() {
        List<BigDecimal> high = iWfDataCfService.queryHigh();
        System.out.println(high);
    }

    //# queryList



    @Test public void
    queryList() {
//        CfListPatternQuery query = CfListPatternQuery.builder()
//                .queryMode("day")
//                .dateStr("2021-06-16")
//                .high("70.000")
//                .pageNumber(1)
//                .pageSize(20)
//                .build();

        CfListPatternQuery query = CfListPatternQuery.builder()
                .queryMode("day")
                .dateStrPre("2021-7-16")
                .dateStrPost("2021-7-16")
                .high("70.000")
                .pageNumber(1)
                .pageSize(20)
                .build();

        Pager<CfListDTO> pager = iWfDataCfService.cfListQuery(query);
        System.out.println(pager);
        System.out.println();
    }

    @Test public void
    queryCurve() {
        CfCurvePatternQuery query = CfCurvePatternQuery.builder()
                .queryMode("day")
                .dateStrPre("2021-6-16")
                .dateStrPost("2021-6-16")
                .high("70.000")
                .build();
        List<CfCurveDTO> cfCurveDTOS = iWfDataCfService.cfCurveQuery(query);
        System.out.println(cfCurveDTOS);
    }

    @Autowired
    private IWfTimeBaseService timeBaseService;

    @Test public void
    batchTimeSave() {
        timeBaseService.generateCurDateData();
    }

    @Autowired
    private IWfAnalyseDqService analyseDqService;

    @Test public void
    anaDq() {
        PowerAnalysisQuery query = new PowerAnalysisQuery();
        query.setPageNumber(1);
        query.setPageSize(3);

        query.setQueryMode("date");
        query.setDateStrPre("2021-06-17");
        query.setDateStrPost("2021-06-17");

        DqListAggregateDTO aggregateDTO = analyseDqService.dqPowerAnalysis(query);
        System.out.println(aggregateDTO);
        System.out.println();
    }

    @Autowired
    private IWfAnalyseCdqService analyseCdqService;

    @Test public void
    anaCdq() {
        PowerAnalysisQuery query = new PowerAnalysisQuery();
        query.setPageNumber(1);
        query.setPageSize(3);

        query.setQueryMode("date");
        query.setDateStrPre("2021-06-17");
        query.setDateStrPost("2021-06-18");

        CdqListAggregateDTO aggregateDTO = analyseCdqService.cdqPowerAnalysis(query);
        System.out.println(aggregateDTO);
        System.out.println();
    }

    @Test public void
    postDate() {
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println();
    }

    //# 自定义查询pager

    @Autowired
    private WfTimeBaseMapper timeBaseMapper;

    @Test public void
    timeBase() {
//        Page<NwpListPatternDTO> page = new Page<>(1, 20);
//        List<NwpListPatternDTO> nwpListPatternDTOS =
//                timeBaseMapper.nwpListPattern(page, "2021-06-17", "2021-06-18", 15);
//        System.out.println(nwpListPatternDTOS);
//        System.out.println();
    }


    @Test public void
    testAnaCrud() {
        LocalDateTime now = LocalDateTime.now();
        QueryWrapper<WfAnalyseDq> analyseDqQueryWrapper = new QueryWrapper<>();
//        analyseDqQueryWrapper.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(now));
        analyseDqQueryWrapper.eq("calc_date", "2021-06-11 00:00:00");
        WfAnalyseDq analyseDq = analyseDqService.getBaseMapper().selectOne(analyseDqQueryWrapper);
        System.out.println(analyseDq);
        System.out.println();
    }

    @Test public void
    time() {
        // 18 19 20 21 22 23
        LocalDate now = LocalDate.now();
//        now = now.minusDays(1);
        System.out.println(now);
        timeBaseService.generateCurDateData(now);

    }


    @Test public void
    pointDate() {
        timeBaseService.generateCurDateData("2021-06-28");
    }

    @Test public void genScaleData() {

        LocalDate parse = LocalDate.parse(
                "2021-07-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        Month month = parse.getMonth();
        for(;month.equals(parse.getMonth());) {

            timeBaseService.generateCurDateData(parse);
            parse = parse.plusDays(1);
        }

    }

    @Test public void
    testAA() {

        int i = 0;

        // 1min 一条
        LocalDateTime now = LocalDateTime.now();

        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);
        // Mock数据
        int windDirect = ThreadLocalRandom.current().nextInt(1, 360);

        int windSpeed = ThreadLocalRandom.current().nextInt(1, 360);

        int pressure = ThreadLocalRandom.current().nextInt(1, 360);

        int temperature = ThreadLocalRandom.current().nextInt(1, 360);

        int humidity = ThreadLocalRandom.current().nextInt(1, 360);

        WfDataCf wfDataCf = WfDataCf.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .windSpeed(new BigDecimal(windSpeed))
                .highLevel(new BigDecimal("70"))
                .windDirection(new BigDecimal(windDirect))
                .temperature(new BigDecimal(temperature))
                .humidity(new BigDecimal(humidity))
                .pressure(new BigDecimal(pressure))
                .turbineHigh(new BigDecimal("30"))
                .calcPower(new BigDecimal(windDirect * 0.618))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        // 存储数据
        iWfDataCfService.save(wfDataCf);
    }


    /*
        数据计算相关
     */

    @Autowired
    private DqCalcService dqCalcService;

    @Autowired
    private CdqCalcService cdqCalcService;

    @Test public void
    calcDq() {
        // 2021-06-23 00:00:00
        // 2021-06-23 00:15:00
        //

        String bg = "2021-06-23 00:00:00";
        String end = "2021-06-23 00:15:00";
        String head = "2021-06-23 00:15:00";
        dqCalcService.dqDataCalc(bg, end, head);
        cdqCalcService.calcData(bg, end, head);
        System.out.println();
    }


    /*
        数据计算相关
     */

    /*
        数据生成
     */

    @Autowired
    private IWfAssessMonthService assessMonthService;

    @Test public void
    genData() {

        ThreadLocalRandom current = ThreadLocalRandom.current();

        LocalDate tmp = LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime parse = LocalDateTime.of(tmp, LocalTime.MIN);
        int count = 12;
        for (int i = 0; i < count; i++) {
            int autoElectric = current.nextInt(11, 300);
            int autoPay = current.nextInt(11, 300);

            int fnlElectric = current.nextInt(11, 300);
            int fnlPay = current.nextInt(11, 300);

            int scheduleElectric = current.nextInt(11, 300);
            int schedulePay = current.nextInt(11, 300);

            WfAssessMonth build = WfAssessMonth
                    .builder()
                    .version(0)
                    .calcDate(parse.plusMonths(i))
                    .autoElectric(new BigDecimal(autoElectric))
                    .autoPay(new BigDecimal(autoPay))
                    .fnlElectric(new BigDecimal(fnlElectric))
                    .fnlPay(new BigDecimal(fnlPay))
                    .scheduleElectric(new BigDecimal(scheduleElectric))
                    .schedulePay(new BigDecimal(schedulePay))
                    .build();

            build.setContrastElectric(
                    build.getAutoElectric().subtract(build.getScheduleElectric())
            );

            build.setContrastPay(
                    build.getAutoPay().subtract(build.getSchedulePay())
            );

            build.setFnlContrastElectric(
                    build.getFnlElectric().subtract(build.getScheduleElectric())
            );

            build.setFnlContrastPay(
                    build.getFnlPay().subtract(build.getSchedulePay())
            );

            assessMonthService.getBaseMapper().insert(build);
        }
        System.out.println();
    }

    /*
        数据生成
     */

    @Test public void sftpDw() throws IOException {
        SFTPUtil sftpUtil = new SFTPUtil();
        sftpUtil.open();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sftpUtil.download("wf/dq", "aac.txt", outputStream);
        byte[] bytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        List<String> list = IOUtils.readLines(inputStream, "GBK");
        System.out.println(list);
        System.out.println();

        dqService.batchProcessDqData(list);

    }

    @Autowired
    private IWfBasicParseResultService parseResultService;

    /*
        生成待获取数据
     */
    @Test public void
    genReady() {

//        LocalDate parse = LocalDate.parse("2021-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        LocalDate parse = LocalDate.parse("2021-06-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Month month = parse.getMonth();
        for (;month.equals(parse.getMonth());) {
            parseResultService.genDqDataNeedFetch(parse);
            parseResultService.genCdqDataNeedFetch(parse);
            parseResultService.genNwpDataNeedFetch(parse);
            parse = parse.plusDays(1);
        }

    }


    @Test public void testSftpFetchData() {
//        2021-05-01 00:00:00
        LocalDate parse = LocalDate.parse("2021-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime dateTime = LocalDateTime.of(parse, LocalTime.MIN);
        parseResultService.fetchDqWithPointDate(dateTime, "dq");

    }


    @Test public void testSftpFetchDqData() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        parseResultService.fetchDqWithPointDate(dateTime, "dq");
    }


    @Test public void triggerPre() {
        parseResultService.reLaunchCurDayPreLoss();
        System.out.println();
    }

    @Test public void reLaunchCurDayPreLoss() {
        parseResultService.relaunchDayBefore();
        System.out.println();
    }

    //# 超短期功率计算测试

//    @Autowired
//    private CdqCalcService cdqCalcService;

    @Test public void calcCdqUse() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        String dateStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(date);

        //
//        String nowStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(now);
        LocalDateTime apply = CalcCommonUtils.timePostRangeProcessRetDateTime.apply(now);
        String nowStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(apply);
        cdqCalcService.calcData(dateStr, nowStr, nowStr);
    }

    @Autowired
    AssessCalcService assessCalcService;

    @Test public void calcLeak() {

        String yesterdayStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assessCalcService.calcYesterdayAssess(yesterdayStr);
    }

}
