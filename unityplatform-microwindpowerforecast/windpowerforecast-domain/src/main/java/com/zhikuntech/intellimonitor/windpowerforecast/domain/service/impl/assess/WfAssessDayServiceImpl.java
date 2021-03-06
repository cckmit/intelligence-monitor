package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.assess;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.ChangeResultDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.DayAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessChange;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAssessDayMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.AssessChangeQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.DayAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessChangeService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessDayService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.AssessCalcUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 日考核结果 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WfAssessDayServiceImpl extends ServiceImpl<WfAssessDayMapper, WfAssessDay> implements IWfAssessDayService {

    private final WfAssessDayMapper dayMapper;

    private final IWfAssessChangeService assessChangeService;

    @Override
    public Pager<DayAssessListDTO> queryDayList(DayAssessQuery query) {
        Pager<DayAssessListDTO> pagerRes = Pager.emptyPager();
        if (Objects.isNull(query)) {
            return pagerRes;
        }
        String date = query.getDate();
        Integer pageNumber = query.getPageNumber();
        Integer pageSize = query.getPageSize();

        // 参数校验
        if (StringUtils.isBlank(date)) {
            return pagerRes;
        }

        // 分页
        Page<DayAssessListDTO> pageCriteria = new Page<>();
        pageCriteria.setCurrent(pageNumber);
        pageCriteria.setSize(pageSize);
        // 条件
        String monthStr = date + "-01";
        LocalDate monthPre = DateProcessUtils.parseToLocalDate(monthStr);
        LocalDate monthPost = monthPre.plusMonths(1);

        String strMonthPre = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthPre);
        String strMonthPost = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthPost);

        // calcDate -> calc_date
        String byFiled = "wad.calc_date";
        String upOrDown = "asc";
        if ("calcDate".equalsIgnoreCase(query.getOderByField())) {
            if ("up".equalsIgnoreCase(query.getUpOrDown())) {
                upOrDown = "asc";
            } else {
                upOrDown = "desc";
            }
        }

        // 查询
        List<DayAssessListDTO> results = dayMapper.dayListPattern(pageCriteria, strMonthPre, strMonthPost,
                byFiled, upOrDown);
        if (CollectionUtils.isNotEmpty(results)) {
            pagerRes.replaceWithNewCol(results);
        }

        return pagerRes;
    }

    @Transactional
    @Override
    public ChangeResultDTO changeAssessData(AssessChangeQuery query) {
        ChangeResultDTO changeResultDTO = ChangeResultDTO.builder()
                .result(1)
                .build();
        // 检验参数
        if (Objects.isNull(query)) {
            changeResultDTO.setMsg("参数不能为空.");
            return changeResultDTO;
        }
        if (Objects.isNull(query.getId())) {
            changeResultDTO.setMsg("数据标识不能为空.");
            return changeResultDTO;
        }
        if (StringUtils.isBlank(query.getChangeReason())) {
            changeResultDTO.setMsg("修改理由不能为空.");
            return changeResultDTO;
        }
        if (StringUtils.isBlank(query.getExecPerson())
                || StringUtils.isBlank(query.getGuardian())) {
            changeResultDTO.setMsg("认证信息均不能为空.");
            return changeResultDTO;
        }
        if (Objects.isNull(query.getDqHiatusChange())
                || Objects.isNull(query.getDqRatioChange())
                || Objects.isNull(query.getCdqHiatusChange())
                || Objects.isNull(query.getCdqRatioChange())) {
            changeResultDTO.setMsg("修改后的数据均不能为空.");
            return changeResultDTO;
        }
        // wf_assess_day#id
        Integer id = query.getId();
        QueryWrapper<WfAssessDay> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("id", id);
        WfAssessDay wfAssessDay = getBaseMapper().selectOne(dayQueryWrapper);
        if (Objects.isNull(wfAssessDay)) {
            changeResultDTO.setMsg("未找到数据标识对应数据.");
            return changeResultDTO;
        }
        // 校验时间范围
        LocalDateTime calcDate = wfAssessDay.getCalcDate();
        if (Objects.nonNull(calcDate)) {
            // 当前时间必须在计算时间之前的90天内及次日的凌晨2点之后才可以修改
            LocalDateTime judgeWithNow = LocalDateTime.now();

            long acrossDays = judgeWithNow.toLocalDate().toEpochDay() - calcDate.toLocalDate().toEpochDay();

            // 次日凌晨2点
            LocalDateTime secondClockTwo = LocalDateTime.of(calcDate.toLocalDate().plusDays(1), LocalTime.of(2, 0));

            int maxBeforeDays = 90;
            if (acrossDays > maxBeforeDays) {
                changeResultDTO.setMsg("只能修改最近[" + maxBeforeDays + "]天的数据.");
                return changeResultDTO;
            }
            if (judgeWithNow.compareTo(secondClockTwo) < 0) {
                changeResultDTO.setMsg(TimeProcessUtils.formatLocalDateTimeWithSecondPattern(secondClockTwo) + "之后可修改该数据");
                return changeResultDTO;
            }
        } else {
            changeResultDTO.setMsg("异常状态,id[" + id + "]对应的时间为空");
            return changeResultDTO;
        }

        //# 查询是否已经存在修改过后的数据
        QueryWrapper<WfAssessChange> changeQueryWrapper = new QueryWrapper<>();
        changeQueryWrapper.eq("day_ref_id", id);
        changeQueryWrapper.eq("newest", 0);
        WfAssessChange historyChange = assessChangeService.getBaseMapper().selectOne(changeQueryWrapper);
        if (Objects.nonNull(historyChange)) {
            historyChange.setNewest(1);
            assessChangeService.updateById(historyChange);
        }

        // 构造新数据
        WfAssessChange assessChange = new WfAssessChange();
        BeanUtils.copyProperties(wfAssessDay, assessChange);

        assessChange.setId(null);
        assessChange.setNewest(0);
        assessChange.setDayRefId(wfAssessDay.getId());

        LocalDateTime now = LocalDateTime.now();
        assessChange.setExecPerson(query.getExecPerson());
        assessChange.setGuardian(query.getGuardian());
        assessChange.setFixReason(query.getChangeReason());
        assessChange.setFixTime(now);
        assessChange.setCreateTime(now);
        if (Objects.nonNull(historyChange)) {
            assessChange.setPreId(historyChange.getId());
        }

        assessChange.setDqHiatus(query.getDqHiatusChange());
        assessChange.setDqRatio(query.getDqRatioChange());
        assessChange.setDqElectric(AssessCalcUtils.calcDqElectricR1(query.getDqRatioChange()));
        assessChange.setDqPay(AssessCalcUtils.calcDqPayR1(query.getDqRatioChange()));

        assessChange.setCdqHiatus(query.getCdqHiatusChange());
        assessChange.setCdqRatio(query.getCdqRatioChange());
        assessChange.setCdqElectric(AssessCalcUtils.calcCdqElectricR1(query.getCdqRatioChange()));
        assessChange.setCdqPay(AssessCalcUtils.calcCdqPayR1(query.getCdqRatioChange()));

        assessChangeService.getBaseMapper().insert(assessChange);

        // TODO 触发日考核电量的计算/月考核电量的计算

        changeResultDTO.setMsg("成功");
        changeResultDTO.setResult(0);
        return changeResultDTO;
    }

}
