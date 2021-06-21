package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfTimeBase;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfTimeBaseMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfTimeBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
        // 生成当日的数据
        this.generateCurDateData(LocalDate.now());
    }

    @Override
    public
    // 同步调用
    synchronized
    void generateCurDateData(LocalDate currentDate) {
        //# 生成前判断数据是否已经存在
        String bgStr = DateProcessUtils.fetchDayBegin(currentDate);
        String endStr = DateProcessUtils.fetchTomorrowBegin(currentDate);
        QueryWrapper<WfTimeBase> timeBaseQueryWrapper = new QueryWrapper<>();
        timeBaseQueryWrapper.gt("date_time", bgStr);
        timeBaseQueryWrapper.le("date_time", endStr);
        List<WfTimeBase> timeBaseList = getBaseMapper().selectList(timeBaseQueryWrapper);
        if (CollectionUtils.isNotEmpty(timeBaseList)) {
            log.warn("时间:[{}]已存在.", currentDate);
            return;
        }
        //# 生成前判断数据是否已经存在

        /*
            时间基准
         */
        LocalTime min = LocalTime.MIN;
        LocalDateTime dayBase = LocalDateTime.of(currentDate, min);

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

    static Pattern yyyyMMddPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    @Override
    public void generateCurDateData(String dateStr) {
        // 格式校验
        dateStr = StringUtils.trim(dateStr);
        boolean matches = yyyyMMddPattern.matcher(dateStr).matches();
        if (!matches) {
            log.warn("时间[{}]不符合格式[yyyy-MM-dd]", dateStr);
            return;
        }

        LocalDate localDate = DateProcessUtils.parseToLocalDate(dateStr);
        this.generateCurDateData(localDate);
    }
}
