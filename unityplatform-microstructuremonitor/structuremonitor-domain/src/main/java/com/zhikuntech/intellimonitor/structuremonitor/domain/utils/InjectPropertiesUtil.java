package com.zhikuntech.intellimonitor.structuremonitor.domain.utils;


import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 代志豪
 * 2021/6/9 10:49
 */
@Slf4j
public class InjectPropertiesUtil<T> {


    public static <T> T injectByAnnotation(T t, RtdbData[] data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);

                int value = null == goldenId ? 0 : goldenId.value();
                for (RtdbData rtdbData : data) {
                    if (value == rtdbData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (field.getDeclaringClass() == BigDecimal.class) {
                                field.set(t, BigDecimal.valueOf(Double.parseDouble(rtdbData.getValue().toString())));
                            } else {
                                field.set(t, rtdbData.getValue());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
            }

        }
        return t;
    }

    public static <T> T injectByAnnotation(T t, List<ValueData> data, List<StructureToGolden> list) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = goldenId.value();
                //筛选相同backid的映射关系实体类
                List<StructureToGolden> collect = list.stream().filter(item -> {
                    return item.getBackenId() == value;
                }).collect(Collectors.toList());
                if (collect.size() != 1) {
                    continue;
                }
                StructureToGolden structureToGolden = collect.get(0);
                for (ValueData valueData : data) {
                    if (structureToGolden.getGoldenId() == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (valueData.getValue() == 0) {
                                field.set(t, valueData.getState());
                            } else {
                                if (field.getDeclaringClass() == BigDecimal.class) {
                                    field.set(t, BigDecimal.valueOf(valueData.getValue()));
                                } else {
                                    field.set(t, valueData.getValue());
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
            }
        }
        return t;
    }
}
