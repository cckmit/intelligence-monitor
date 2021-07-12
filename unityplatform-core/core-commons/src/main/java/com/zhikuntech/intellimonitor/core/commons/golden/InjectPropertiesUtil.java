package com.zhikuntech.intellimonitor.core.commons.golden;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 10:49
 */
@Slf4j
public class InjectPropertiesUtil<T> {

    public static <T> T injectByAnnotation(T t, Integer num, RtdbData[] data, GetCache cache) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : getGoldenId(cache, goldenId.value(), num);
            for (RtdbData rtdbData : data) {
                if (value == rtdbData.getId()) {
                    try {
                        field.setAccessible(true);
                        field.set(t, new BigDecimal(rtdbData.getValue().toString()).setScale(2, RoundingMode.HALF_UP));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return t;
    }

    public static <T> List<T> injectByAnnotation(List<T> t, RtdbData[] data, GetCache cache) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < t.size(); i++) {
            for (Field field : fields) {
                T item = t.get(i);
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : getGoldenId(cache, goldenId.value(), i + 1);
                for (RtdbData rtdbData : data) {
                    if (value == rtdbData.getId()) {
                        try {
                            field.setAccessible(true);
                            field.set(item, new BigDecimal(rtdbData.getValue().toString()).setScale(2, RoundingMode.HALF_UP));
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

    public static <T> List<T> injectByAnnotation(List<T> t, List<ValueData> data, GetCache cache) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < t.size(); i++) {
            for (Field field : fields) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                T item = t.get(i);
                int value = null == goldenId ? 0 : getGoldenId(cache, goldenId.value(), i + 1);
                for (ValueData valueData : data) {
                    if (value == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            if (valueData.getValue() == 0) {
                                field.set(item, new BigDecimal(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
                            } else {
                                field.set(item, BigDecimal.valueOf(valueData.getValue()).setScale(2, RoundingMode.HALF_UP));
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

    public static <T> T injectByAnnotation(T t, Integer num, List<ValueData> data, GetCache cache) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : getGoldenId(cache, goldenId.value(), num);
            for (ValueData valueData : data) {
                if (value == valueData.getId()) {
                    try {
                        field.setAccessible(true);
                        if (valueData.getValue() == 0) {
                            field.set(t, new BigDecimal(valueData.getState()).setScale(2, RoundingMode.HALF_UP));
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
        return t;
    }


    /**
     * 获取该字段所映射的goldenId
     */
    private static int getGoldenId(GetCache cache, Integer value, Integer fanNumber) {
        String redisKey;
        if (null == fanNumber) {
            redisKey = FanConstant.GOLDEN_ID + value;
        } else {
            redisKey = FanConstant.GOLDEN_ID + value + "_" + fanNumber;
        }
        Integer integer = cache.getValue(redisKey);
        return null == integer ? 0 : integer;
    }
}
