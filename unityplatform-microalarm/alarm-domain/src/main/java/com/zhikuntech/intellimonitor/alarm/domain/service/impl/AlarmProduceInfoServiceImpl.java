package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmProduceInfoMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmConfirmQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
        List<AlarmProduceInfo> alarmProduceInfos = getBaseMapper().selectList(produceInfoQueryWrapper);
        if (CollectionUtils.isEmpty(alarmProduceInfos)) {
            return Collections.emptyList();
        }
        final List<AlarmStatusGroupByModuleDTO> moduleDTOList = new ArrayList<>();
        // todo 转换数据
        alarmProduceInfos.stream()
                .filter(Objects::nonNull)
                .filter(p -> Objects.nonNull(p.getGroupType()))
                .collect(Collectors.groupingBy(AlarmProduceInfo::getGroupType));
        return moduleDTOList;
    }

    @Override
    public Pager<AlarmInfoDTO> queryByPage(AlarmInfoSimpleQuery simpleQuery) {
        // todo

        return null;
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
}
