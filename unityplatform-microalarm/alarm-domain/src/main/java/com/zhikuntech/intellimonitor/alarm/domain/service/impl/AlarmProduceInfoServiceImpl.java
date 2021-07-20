package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoBatchDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmProduceInfoMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmConfirmQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoLimitQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 告警信息 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Slf4j
@Service
public class AlarmProduceInfoServiceImpl extends ServiceImpl<AlarmProduceInfoMapper, AlarmProduceInfo> implements IAlarmProduceInfoService {


    @Override
    public List<AlarmStatusGroupByModuleDTO> fetchStatusAllGroup() {
        QueryWrapper<AlarmProduceInfo> produceInfoQueryWrapper = new QueryWrapper<>();
        produceInfoQueryWrapper.isNull("next_info_no");
        produceInfoQueryWrapper.eq("has_restore", 0);
        List<AlarmProduceInfo> alarmProduceInfos = getBaseMapper().selectList(produceInfoQueryWrapper);
        if (CollectionUtils.isEmpty(alarmProduceInfos)) {
            return Collections.emptyList();
        }
        final List<AlarmStatusGroupByModuleDTO> moduleDTOList = new ArrayList<>();

        final Map<Integer, Integer> groupTypeWithAlarmMap = alarmProduceInfos.stream()
                .filter(Objects::nonNull)
                .filter(p -> Objects.nonNull(p.getGroupType()))
                .collect(Collectors.groupingBy(AlarmProduceInfo::getGroupType, Collectors.reducing(0, e -> 1, Integer::sum)));
        assert MapUtils.isNotEmpty(groupTypeWithAlarmMap);
        groupTypeWithAlarmMap.forEach((k, v) -> {
            AlarmStatusGroupByModuleDTO statusGroupByModuleDTO = new AlarmStatusGroupByModuleDTO();
            statusGroupByModuleDTO.setGroupType(k);
            statusGroupByModuleDTO.setAlarmNum(v);
            moduleDTOList.add(statusGroupByModuleDTO);
        });
        return moduleDTOList;
    }

    @Override
    public Pager<AlarmInfoDTO> queryByPage(AlarmInfoSimpleQuery simpleQuery) {

        throw new UnsupportedOperationException("告警信息暂不提供分页查询");
    }


    @Override
    public List<AlarmInfoBatchDTO> fetchBatchLimit(AlarmInfoLimitQuery limitQuery) {
        if (Objects.isNull(limitQuery)) {
            throw new IllegalArgumentException("查询参数必须");
        }
        final Integer queryType = limitQuery.getQueryType();
        if (Objects.isNull(limitQuery.getQueryType())) {
            throw new IllegalArgumentException("查询类型必须");
        }
        if (Objects.isNull(limitQuery.getDataNum()) || Objects.isNull(limitQuery.getRowNum())) {
            throw new IllegalArgumentException("(数据条数,行号)必须");
        }
        if (limitQuery.getDataNum() > 30) {
            throw new IllegalArgumentException("单次获取的最大数据量不能超过30行");
        }

        List<AlarmProduceInfo> alarmProduceInfos = null;
        if (queryType == 1) {
            /*
                待确认重要告警:
                    criteria
                    1.当前告警(非历史告警)
                    2.严重告警
             */
            QueryWrapper<AlarmProduceInfo> produceInfoQueryWrapper = new QueryWrapper<>();
            produceInfoQueryWrapper.gt("row_stamp", limitQuery.getRowNum());
            produceInfoQueryWrapper.eq("alarm_level", "严重");
            produceInfoQueryWrapper.eq("with_history", 0);
            produceInfoQueryWrapper.last(String.format("limit %d", limitQuery.getDataNum()));
            alarmProduceInfos = getBaseMapper().selectList(produceInfoQueryWrapper);
        } else if (queryType == 0) {
            /*
                查询全部告警
             */
            QueryWrapper<AlarmProduceInfo> produceInfoQueryWrapper = new QueryWrapper<>();
            produceInfoQueryWrapper.gt("row_stamp", limitQuery.getRowNum());
            produceInfoQueryWrapper.last(String.format("limit %d", limitQuery.getDataNum()));
            alarmProduceInfos = getBaseMapper().selectList(produceInfoQueryWrapper);
        } else {
            throw new IllegalArgumentException("不能识别的查询类型:[" + queryType + "]");
        }

        if (CollectionUtils.isEmpty(alarmProduceInfos)) {
            return Collections.emptyList();
        }
        // 转换数据
        List<AlarmInfoBatchDTO> results = new ArrayList<>(alarmProduceInfos.size());
        for (AlarmProduceInfo produceInfo : alarmProduceInfos) {
            AlarmInfoBatchDTO tmp = AlarmInfoBatchDTO.builder()
                    .alarmInfoNo(produceInfo.getInfoNo())
                    .alarmTime(produceInfo.getAlarmDate())
                    .alarmDescribe(produceInfo.getAlarmDescribe())
                    //# 需要查询规则
                    .alarmLevel(produceInfo.getRuleNo())
                    .alarmType(produceInfo.getRuleNo())
                    //# 需要查询规则
                    .confirm(produceInfo.getHasConfirm())
                    .confirmPerson(produceInfo.getConfirmPerson())
                    .confirmTime(produceInfo.getConfirmTime())
                    .restoreTime(produceInfo.getRestoreTime())
                    .rowNum(produceInfo.getRowStamp())
                    .build();
            results.add(tmp);
        }
        return results;
    }

    @Override
    public boolean alarmConfirm(AlarmConfirmQuery query) {
        log.info("告警确认, 入参[{}]", query);
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (Objects.isNull(query.getConfirmType())) {
            throw new IllegalArgumentException("确认类型必传");
        }
        final String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        final String confirmPerson = query.getConfirmPerson();
        if (Integer.valueOf(1).equals(query.getConfirmType())) {
            Integer pageType = query.getPageType();
            if (Objects.isNull(pageType)) {
                throw new IllegalArgumentException("页面类型不能为空");
            }
            // 页面确认应该是确认当前告警
            UpdateWrapper<AlarmProduceInfo> infoUpdateWrapper = new UpdateWrapper<>();
            infoUpdateWrapper.setSql(String.format("confirm_time = %s, hasConfirm = %d, confirm_person = %s", nowStr, 1, confirmPerson));
            infoUpdateWrapper.isNull("next_info_no");
            infoUpdateWrapper.eq("group_type", query.getPageType());
            int update = getBaseMapper().update(null, infoUpdateWrapper);
            assert update != 0;
        } else if (Integer.valueOf(0).equals(query.getConfirmType())) {
            List<String> infoNos = query.getInfoNos();
            if (CollectionUtils.isEmpty(infoNos)) {
                throw new IllegalArgumentException("需要确认的告警信息编码不能为空");
            }
            UpdateWrapper<AlarmProduceInfo> infoUpdateWrapper = new UpdateWrapper<>();
            infoUpdateWrapper.setSql(String.format("confirm_time = %s, hasConfirm = %d, confirm_person = %s", nowStr, 1, confirmPerson));
            infoUpdateWrapper.in("info_no", infoNos);
            int update = getBaseMapper().update(null, infoUpdateWrapper);
            assert update != 0;
        } else {
            throw new IllegalArgumentException("未能识别的类型");
        }
        return true;
    }

    @Override
    public AlarmProduceInfo fetchCurAlarmInfoByMonitorNo(String monitorNo) {
        QueryWrapper<AlarmProduceInfo> produceInfoQueryWrapper = new QueryWrapper<>();
        produceInfoQueryWrapper.eq("monitor_no", monitorNo);
        produceInfoQueryWrapper.eq("with_history", 0);
        AlarmProduceInfo produceInfo = getBaseMapper().selectOne(produceInfoQueryWrapper);
        if (log.isDebugEnabled()) {
            log.debug("查询条件:[{}], 返回结果:[{}]", monitorNo, produceInfo);
        }
        return produceInfo;
    }

}
