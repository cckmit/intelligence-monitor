package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller;

import com.zhikuntech.intellimonitor.demo.facade.DemoFacade;
import com.zhikuntech.intellimonitor.demo.prototype.model.DemoModel;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfBasicParseResultService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfTimeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * @author liukai
 */
@RequestMapping("/test")
@RestController
public class TestController {

    private final DemoFacade demoFacade;

    public TestController(DemoFacade demoFacade, IWfBasicParseResultService parseResultService, IWfTimeBaseService iWfTimeBaseService) {
        this.demoFacade = demoFacade;
        this.parseResultService = parseResultService;
        this.iWfTimeBaseService = iWfTimeBaseService;
    }

    @GetMapping("/facade")
    public DemoModel demo() {
        return demoFacade.obtainColor();
    }

    private final IWfBasicParseResultService parseResultService;
    @GetMapping("/year2021")//生成2020，2021，2022年的文件名称表和时间基准表
    public void year() {
        for (int j=2020;j<=2022;j++){
            for (int i=1;i<=12;i++) {
                int monthInt = i;
                String Str;
                if (monthInt <= 9) {
                    Str = j+"-0" + monthInt + "-01";
                } else {
                    Str = j+"-" + monthInt + "-01";
                }
                LocalDate parse = LocalDate.parse(Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Month month = parse.getMonth();
                for (; month.equals(parse.getMonth()); ) {
                    parseResultService.genDqDataNeedFetch(parse);
                    parseResultService.genCdqDataNeedFetch(parse);
                    parseResultService.genNwpDataNeedFetch(parse);
                    iWfTimeBaseService.generateCurDateData(parse);
                    parse = parse.plusDays(1);
                }
            }
        }
    }
    private final IWfTimeBaseService iWfTimeBaseService;
    @GetMapping("/timeYear2021")//生成2021年的时间基准表
    public void timeBaseYear() throws IOException {
        for (int i=1;i<=12;i++) {
            int monthInt = i;
            String Str;
            if (monthInt <= 9) {
                Str = "2021-0" + monthInt + "-01";
            } else {
                Str = "2021-" + monthInt + "-01";
            }
            LocalDate parse = LocalDate.parse(Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Month month = parse.getMonth();
            for (; month.equals(parse.getMonth()); ) {
                iWfTimeBaseService.generateCurDateData(parse);
                parse = parse.plusDays(1);
            }
        }
    }


}
