package com.zhikuntech.intellimonitor.mainpage.domain.golden;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public static <T> List<T> injectByAnnotation(List<T> t, RtdbData[] data) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (T item : t) {
            for (Field field : fields) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : goldenId.value();
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
        for (T item : t) {
            for (Field field : fields) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : goldenId.value();
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

    public static <T> T injectByAnnotation(T t, List<ValueData> data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
            int value = null == goldenId ? 0 : goldenId.value();
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
}
