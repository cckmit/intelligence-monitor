package com.zhikuntech.intellimonitor.fanscada.domain.golden;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.fanscada.domain.config.StartUpInitForGoldenId;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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

    private static final String BASE_PACKAGE = "com.zhikuntech.intellimonitor.fanscada.domain.vo";


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


    public static <T> List<T> injectByAnnotationForBigdecimal(List<T> t, RtdbData[] data) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (T item : t) {
            for (Field field : fields) {
                if (field.getAnnotation(GoldenId.class) != null) {
                    GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                    int value = null == goldenId ? 0 : goldenId.value();
                    Integer fanNumber = ((FanBaseInfoVO) item).getFanNumber();
                    //获取该字段所映射的golden id
                    String key = FanConstant.GOLDEN_ID + value + "_" + fanNumber;
                    Integer id = StartUpInitForGoldenId.initMap.get(key);
                    for (RtdbData rtdbData : data) {
                        if (id == rtdbData.getId()) {
                            try {
                                field.setAccessible(true);
                                if (field.getType().equals(BigDecimal.class)) {
                                    field.set(item, BigDecimal.valueOf(Double.parseDouble(rtdbData.getValue().toString())));
                                } else {
                                    field.set(item, Integer.parseInt(rtdbData.getValue().toString()));
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

    public static <T> T injectByAnnotation(T t, List<ValueData> data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : goldenId.value();
                for (ValueData valueData : data) {
                    if (value == valueData.getId()) {
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

    public static <T> List<T> injectByAnnotationForBigdecimal(List<T> t, List<ValueData> data) {
        Field[] fields = t.get(0).getClass().getDeclaredFields();
        for (T item : t) {
            for (Field field : fields) {
                if (field.getAnnotation(GoldenId.class) != null) {
                    GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                    int value = null == goldenId ? 0 : goldenId.value();
                    Integer fanNumber = ((FanBaseInfoVO) item).getFanNumber();
                    //获取该字段所映射的golden id
                    String key = FanConstant.GOLDEN_ID + value + "_" + fanNumber;
                    Integer id = StartUpInitForGoldenId.initMap.get(key);
                    for (ValueData ValueData : data) {
                        if (id == ValueData.getId()) {
                            try {
                                field.setAccessible(true);
                                if (field.getType().equals(BigDecimal.class)) {
                                    field.set(item, BigDecimal.valueOf(Double.parseDouble(ValueData.getValue().toString())));
                                } else {
                                    field.set(item, Integer.parseInt(ValueData.getValue().toString()));
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

    /**
     * @param t      映射实体
     * @param number 风机编号
     * @param mapper BackendToGoldenMapper
     * @return : T
     * @date: Creat in 2021/6/21 11:43
     * @describe: 获取庚顿数据
     */
    public static <T> T injectByAnnotationCustomize(T t, String number, BackendToGoldenMapper mapper, GoldenUtil goldenUtil) {
        long start = System.currentTimeMillis();
        // 获取注解的backendList
        Field[] fields = t.getClass().getDeclaredFields();
        List<Integer> backendList = new ArrayList<>();
        try {
            for (Field field : fields) {
                GoldenId annotation = field.getAnnotation(GoldenId.class);
                field.setAccessible(true);
                if (annotation != null) {
                    backendList.add(annotation.value());
                    // 初始化赋值，避免null
                    field.set(t, dataProcess(0.00, field.getType()));
                } else {
                    Class<?> clazz = field.getType();
                    // 判断该类属于哪个包
                    String pack = clazz.getPackage().getName();
                    if (BASE_PACKAGE.equals(pack)) {
                        // 回调获取某个字段的值
                        Object obj = injectByAnnotationCustomize(clazz.newInstance(), number, mapper, goldenUtil);
                        field.set(t, obj);
                    }
                }

            }
            if (CollectionUtils.isEmpty(backendList)) {
                return t;
            }
            // 通过风机编号查询GoldenIdList
            List<Integer> goldenIdList = mapper.getGoldenIdByWindNumberAndId(backendList);
            int[] goldenIds = goldenIdList.stream().mapToInt(Integer::intValue).toArray();

            QueryWrapper<BackendToGolden> query = new QueryWrapper<>();
            query.eq("number", number);
            query.in("backendId", backendList);
            List<BackendToGolden> backendToGoldenList = mapper.selectList(query);
            if (CollectionUtils.isEmpty(backendToGoldenList)) {
                return t;
            }
            List<ValueData> valueDataList = goldenUtil.getSnapshots(goldenIds);
            for (Field field : fields) {
                GoldenId annotation = field.getAnnotation(GoldenId.class);
                if (annotation == null) {
                    continue;
                }
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
                    if (valueData.getId() == backend.getGoldenId()) {
                        field.setAccessible(true);
                        field.set(t, dataProcess(valueData.getValue(), field.getType()));
                    }
                }
            }
            log.info("##############injectByAnnotationCustomize方法执行完毕，用时：{}{}", System.currentTimeMillis() - start, "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取庚顿数据异常", e);
        }
        return t;
    }

    private static Object dataProcess(Double dataValue, Class<?> fieldType) {
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
