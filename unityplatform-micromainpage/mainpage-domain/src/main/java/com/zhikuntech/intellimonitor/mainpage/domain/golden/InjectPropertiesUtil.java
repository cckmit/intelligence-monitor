package com.zhikuntech.intellimonitor.mainpage.domain.golden;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 10:49
 */
@Slf4j
public class InjectPropertiesUtil<T> {
    public static <T> T injectByAnnotation(T t, RtdbData[] data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : goldenId.value();
            for (RtdbData rtdbData : data) {
                if (value == rtdbData.getId()) {
                    try {
                        field.setAccessible(true);
                        field.set(t, rtdbData.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return t;
    }

    public static <T> T injectByAnnotation(T t, List<ValueData> data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : goldenId.value();
            for (ValueData valueData : data) {
                if (value == valueData.getId()) {
                    try {
                        field.setAccessible(true);
                        field.set(t, valueData.getValue() == 0 ? valueData.getState() : valueData.getDate());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return t;
    }
}
