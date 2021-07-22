package com.zhikuntech.intellimonitor.structuremonitor.domain.utils;


import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.structuremonitor.domain.constant.DataConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.init.MyStartUp;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenAvg;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMax;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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

    public static <T> List<T> injectByAnnotation(List<T> t, List<ValueData> data) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (int i = 1; i <= t.size(); i++) {
            for (Field field : fields) {
                if (field.getAnnotation(GoldenId.class) != null) {
                    GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                    int value = goldenId.value();
                    Integer gid = MyStartUp.initMap.get(DataConstant.STRUCTURE_TO_GOLDEN + 1 + "_" + value);
                    for (ValueData valueData : data) {
                        if (gid == valueData.getId()) {
                            try {
                                field.setAccessible(true);
                                if (valueData.getValue() == 0) {
                                    field.set(t.get(i - 1), BigDecimal.valueOf(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
                                } else {
                                    field.set(t.get(i - 1), BigDecimal.valueOf(valueData.getValue()).setScale(2, RoundingMode.HALF_UP));
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }
                }
            }
        }

        return t;
    }


    public static <T> T injectByAnnotationMin(T t, List<ValueData> data, List<StructureToGoldenMin> list) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getDeclaringClass().equals(Date.class)){
                try {
                    field.set(t,data.get(0).getDate());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = goldenId.value();
                //筛选相同backid的映射关系实体类
                List<StructureToGoldenMin> collect = list.stream().filter(item -> {
                    return item.getBackenId() == value;
                }).collect(Collectors.toList());
                if (collect.size() != 1) {
                    continue;
                }
                StructureToGoldenMin structureToGoldenMin = collect.get(0);
                for (ValueData valueData : data) {
                    if (structureToGoldenMin.getGoldenId() == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (valueData.getValue() == 0) {
                                field.set(t, BigDecimal.valueOf(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
                            } else {
                                field.set(t, BigDecimal.valueOf(valueData.getValue()).setScale(2, RoundingMode.HALF_UP));
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

    public static <T> T injectByAnnotationAvg(T t, List<ValueData> data, List<StructureToGoldenAvg> list) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getDeclaringClass().equals(Date.class)){
                try {
                    field.set(t,data.get(0).getDate());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = goldenId.value();
                //筛选相同backid的映射关系实体类
                List<StructureToGoldenAvg> collect = list.stream().filter(item -> {
                    return item.getBackenId() == value;
                }).collect(Collectors.toList());
                if (collect.size() != 1) {
                    continue;
                }
                StructureToGoldenAvg structureToGoldenAvg = collect.get(0);
                for (ValueData valueData : data) {
                    if (structureToGoldenAvg.getGoldenId() == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (valueData.getValue() == 0) {
                                field.set(t, BigDecimal.valueOf(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
                            } else {
                                field.set(t, BigDecimal.valueOf(valueData.getValue()).setScale(2, RoundingMode.HALF_UP));
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

    public static <T> T injectByAnnotationMax(T t, List<ValueData> data, List<StructureToGoldenMax> list) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getDeclaringClass().equals(Date.class)){
                try {
                    field.set(t,data.get(0).getDate());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = goldenId.value();
                //筛选相同backid的映射关系实体类
                List<StructureToGoldenMax> collect = list.stream().filter(item -> {
                    return item.getBackenId() == value;
                }).collect(Collectors.toList());
                if (collect.size() != 1) {
                    continue;
                }
                StructureToGoldenMax structureToGoldenMax = collect.get(0);
                for (ValueData valueData : data) {
                    if (structureToGoldenMax.getGoldenId() == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (valueData.getValue() == 0) {
                                field.set(t, BigDecimal.valueOf(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
                            } else {
                                field.set(t, BigDecimal.valueOf(valueData.getValue()).setScale(2, RoundingMode.HALF_UP));
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
