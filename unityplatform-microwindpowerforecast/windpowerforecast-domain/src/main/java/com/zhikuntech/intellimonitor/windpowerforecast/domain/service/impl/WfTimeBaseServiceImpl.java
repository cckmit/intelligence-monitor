package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfTimeBase;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfTimeBaseMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfTimeBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <p>
 * 时间基准表 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@Slf4j
@Service
public class WfTimeBaseServiceImpl extends ServiceImpl<WfTimeBaseMapper, WfTimeBase> implements IWfTimeBaseService {

    @Override
    public void generateCurDateData() {

        /*
            时间基准
         */
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime dayBase = LocalDateTime.of(now, min);

        /*
            一分钟
         */
        List<WfTimeBase> baseMinuteOne = new ArrayList<>();
        int oneStepOfDay = 60 * 24;
        IntStream.rangeClosed(1, oneStepOfDay).forEach(item -> {
            LocalDateTime ldOne = dayBase.plusMinutes(item * 1);
            WfTimeBase timeBase = WfTimeBase.builder()
                    .timeRatio(1)
                    .dateTime(ldOne)
                    .build();
            baseMinuteOne.add(timeBase);
        });

        /*
            五分钟
         */
        int fiveStepOfDay = 12 * 24;
        List<WfTimeBase> baseMinuteFive = new ArrayList<>();
        IntStream.rangeClosed(1, fiveStepOfDay).forEach(item -> {
            LocalDateTime ldFive = dayBase.plusMinutes(item * 5);
            WfTimeBase timeBase = WfTimeBase.builder()
                    .timeRatio(5)
                    .dateTime(ldFive)
                    .build();
            baseMinuteFive.add(timeBase);
        });

        /*
            十五分钟
         */
        int fifStepOfDay = 4 * 24;
        List<WfTimeBase> baseMinuteFif = new ArrayList<>();
        IntStream.rangeClosed(1, fifStepOfDay).forEach(item -> {
            LocalDateTime ldFive = dayBase.plusMinutes(item * 15);
            WfTimeBase timeBase = WfTimeBase.builder()
                    .timeRatio(15)
                    .dateTime(ldFive)
                    .build();
            baseMinuteFif.add(timeBase);
        });


        // store
        saveBatch(baseMinuteOne);
        saveBatch(baseMinuteFive);
        saveBatch(baseMinuteFif);
    }

    @Override
    public void generateCurDateData(LocalDate localDate) {
        // TODO
    }

    @Override
    public void generateCurDateData(String localDate) {
        // TODO
    }
}
