package com.zhikuntech.intellimonitor.onlinemonitor.domain.utils;

import org.springframework.beans.BeanUtils;

public class BeanConvertUtils {

    /**
     * 类型转化
     */
    public static <Target>Target copyProperties(Object source, Class<Target> targetClass){
        try {
            if(source==null || targetClass==null){
                return null;
            }
            Target doInstance = targetClass.newInstance();
            BeanUtils.copyProperties(source, doInstance);
            return doInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
