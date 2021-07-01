package com.zhikuntech.intellimonitor.mainpage.domain.golden;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.FanInfoInit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 10:49
 */
@Slf4j
public class CompareUtil<T> {


    public static <T> T injectByAnnotation(T t, Integer num, RtdbData[] data) {
        Field[] fields = t.getClass().getDeclaredFields();
        Comparator<Double> comparator = Double::compareTo;
        for (Field field : fields) {
            GoldenId golden = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == golden ? 0 : getGoldenId(golden.value(), num);
            String scope = null == golden ? "" : golden.scope();
            for (RtdbData rtdbData : data) {
                if (value == rtdbData.getId()) {
                    if (StringUtils.isNotEmpty(scope)) {
                        String[] split = scope.split(",");
                        int compare = comparator.compare((double) rtdbData.getValue(), Double.parseDouble(split[1])) +
                                comparator.compare((double) rtdbData.getValue(), Double.parseDouble(split[0]));
                        if (compare != 0) {
                            //不等于0，表示不正常
                        }
                    }
                }
            }
        }
        return t;
    }

    public static <T> List<T> injectByAnnotation(List<T> t, RtdbData[] data) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < t.size(); i++) {
            for (Field field : fields) {
                T item = t.get(i);
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : getGoldenId(goldenId.value(), i + 1);
                for (RtdbData rtdbData : data) {
                    if (value == rtdbData.getId()) {
                        try {
                            field.setAccessible(true);
                            field.set(item, rtdbData.getValue());
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
        for (int i = 0; i < t.size(); i++) {
            for (Field field : fields) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                T item = t.get(i);
                int value = null == goldenId ? 0 : getGoldenId(goldenId.value(), i + 1);
                for (ValueData valueData : data) {
                    if (value == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (field.getType().equals(BigDecimal.class)) {
                                if (valueData.getValue() == 0) {
                                    field.set(item, new BigDecimal(valueData.getState()));
                                } else {
                                    field.set(item, BigDecimal.valueOf(valueData.getValue()));
                                }
                            } else {
                                if (valueData.getValue() == 0) {
                                    field.set(item, valueData.getState());
                                } else {
                                    field.set(item, valueData.getValue());
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

    public static <T> T injectByAnnotation(T t, Integer num, List<ValueData> data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : getGoldenId(goldenId.value(), num);
            for (ValueData valueData : data) {
                if (value == valueData.getId()) {
                    try {
                        field.setAccessible(true);
                        if (field.getType().equals(BigDecimal.class)) {
                            if (valueData.getValue() == 0) {
                                field.set(t, new BigDecimal(valueData.getState()));
                            } else {
                                field.set(t, BigDecimal.valueOf(valueData.getValue()));
                            }
                        } else {
                            if (valueData.getValue() == 0) {
                                field.set(t, valueData.getState());
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
        return t;
    }


    /**
     * 获取该字段所映射的goldenId
     */
    private static int getGoldenId(Integer value, Integer fanNumber) {
        String redisKey;
        if (null == fanNumber) {
            redisKey = FanConstant.GOLDEN_ID + value;
        } else {
            redisKey = FanConstant.GOLDEN_ID + value + "_" + fanNumber;
        }
        Integer integer = FanInfoInit.GOLDEN_ID_MAP.get(redisKey);
        return null == integer ? 0 : integer;
    }
}
