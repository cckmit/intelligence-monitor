package com.zhikuntech.intellimonitor.core.commons.golden;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 10:49
 */
@Slf4j
public class InjectPropertiesUtil<T> {

    private static final String FANSCADA_BASE_PACKAGE = "com.zhikuntech.intellimonitor.fanscada.domain.vo";


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
                                field.set(item, new BigDecimal(valueData.getState()));
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
                            field.set(t, new BigDecimal(valueData.getState()));
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

    /**
     * @param t 映射实体
     * @return : T
     * @date: Creat in 2021/6/21 11:43
     * @describe: 获取实体的注解信息
     */
    public static <T> List<Integer> injectByAnnotationCustomize(T t) {
        // 获取注解的backendList
        Field[] fields = t.getClass().getDeclaredFields();
        List<Integer> backendList = new ArrayList<>();
        for (Field field : fields) {
            GoldenId annotation = field.getDeclaredAnnotation(GoldenId.class);
            field.setAccessible(true);
            if (annotation != null) {
                backendList.add(annotation.value());
            } else {
                Class<?> clazz = field.getType();
                // 判断该类属于哪个包
                String pack = clazz.getPackage().getName();
                if (FANSCADA_BASE_PACKAGE.equals(pack)) {
                    // 回调获取某个字段的值
                    Object obj = null;
                    try {
                        backendList.addAll(injectByAnnotationCustomize(clazz.newInstance()));
                    } catch (Exception e) {
                        log.error("获取实体注解失败", e);
                    }
                }
            }
        }
        return backendList;
    }

    /**
     * 初始化赋值
     */
    public static Object dataProcess(Double dataValue, Class<?> fieldType) {
        Object obj = null;
        if (BigDecimal.class.equals(fieldType)) {
            obj = BigDecimal.valueOf(dataValue).setScale(2, RoundingMode.HALF_UP);
        } else if (Integer.class.equals(fieldType)) {
            obj = dataValue.intValue();
        } else if (String.class.equals(fieldType)) {
            String value = String.valueOf(dataValue);
            obj = "0.00".equals(value) ? "0" : value;
        }
        return obj;
    }

}
