package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： DAI
 * @date： Created in 2021/7/21 14:01
 */
@Service
@Slf4j
public class FanDetailServiceImpl implements FanDetailService {

    @Resource
    private BackendToGoldenMapper backend;

    @Override
    public <T> T getMatchData(T t, List<Integer> backendList, String number) {
        List<Integer> goldenIdList = backend.getGoldenIdByWindNumberAndId(backendList);
        int[] goldenIds = goldenIdList.stream().mapToInt(Integer::intValue).toArray();
        QueryWrapper<BackendToGolden> query = new QueryWrapper<>();
        query.eq("number", number);
        query.in("backendId", backendList);
        List<BackendToGolden> backendToGoldenList = backend.selectList(query);
        try {
            List<ValueData> valueDataList = GoldenUtil.getSnapshots(goldenIds);
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                GoldenId annotation = field.getAnnotation(GoldenId.class);
                if (annotation.required()) {
                    Object obj = getMatchData(field.getType().newInstance(), backendList, number);
                    field.set(t, obj);
                    continue;
                }
                // 设置初值
                field.set(t, InjectPropertiesUtil.dataProcess(0.00, field.getType()));
                int fieldValue = annotation.value();
                BackendToGolden backend = null;
                for (BackendToGolden backendToGolden : backendToGoldenList) {
                    if (backendToGolden.getBackendId() == fieldValue) {
                        backend = backendToGolden;
                        break;
                    }
                }
                if (backend == null) {
                    continue;
                }
                for (ValueData valueData : valueDataList) {
                    // 实体字段匹配
                    if (valueData.getId() == backend.getGoldenId()) {
                        field.set(t, InjectPropertiesUtil.dataProcess(valueData.getValue(), field.getType()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取快照数据失败", e);
            e.printStackTrace();
        }
        return t;
    }
}
