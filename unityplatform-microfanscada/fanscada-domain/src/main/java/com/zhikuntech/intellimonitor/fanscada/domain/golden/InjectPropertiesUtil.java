package com.zhikuntech.intellimonitor.fanscada.domain.golden;


import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import com.zhikuntech.intellimonitor.fanscada.domain.service.impl.BackendToGoldenServiceImpl;
import com.zhikuntech.intellimonitor.fanscada.domain.utils.RedisUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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
                    String redisKey = FanConstant.GOLDEN_ID +value+"_"+fanNumber;
                    RedisUtil redisUtil = new RedisUtil();
                    String string = redisUtil.getString(redisKey);
                    int id;
                    if (StringUtils.isBlank(string)){
                        BackendToGoldenService backendToGoldenService = new BackendToGoldenServiceImpl();
                         id = backendToGoldenService.getGoldenIdByNumberAndId(fanNumber, value);
                    }else {
                        id = Integer.parseInt(string);
                    }
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
                    String redisKey = FanConstant.GOLDEN_ID +value+"_"+fanNumber;
                    RedisUtil redisUtil = new RedisUtil();
                    String string = redisUtil.getString(redisKey);
                    int id;
                    if (StringUtils.isBlank(string)){
                        BackendToGoldenService backendToGoldenService = new BackendToGoldenServiceImpl();
                        id = backendToGoldenService.getGoldenIdByNumberAndId(fanNumber, value);
                    }else {
                        id = Integer.parseInt(string);
                    }
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

    public static <T> T injectByAnnotationCustomize(T t, List<ValueData> data) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(GoldenId.class) != null) {
                GoldenId goldenId = field.getDeclaredAnnotation(GoldenId.class);
                int value = null == goldenId ? 0 : goldenId.value();
                for (ValueData valueData : data) {
                    if (value == valueData.getId()) {
                        try {
                            field.setAccessible(true);
                            Double dataValue = valueData.getValue();
                            Class<?> fieldType = field.getType();
                            if (BigDecimal.class.equals(fieldType)) {
                                BigDecimal bigDecimal = BigDecimal.valueOf(dataValue).setScale(2, RoundingMode.HALF_UP);
                                field.set(t, bigDecimal);
                            } else if (Integer.class.equals(fieldType)) {
                                field.set(t, dataValue.intValue());
                            } else if (String.class.equals(fieldType)) {
                                field.set(t, String.valueOf(dataValue));
                            } else {
                                field.set(t, null);
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
