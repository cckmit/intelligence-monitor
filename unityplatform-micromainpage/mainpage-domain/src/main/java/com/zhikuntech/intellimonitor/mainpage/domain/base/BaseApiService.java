package com.zhikuntech.intellimonitor.mainpage.domain.base;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.CommonBeanUtils;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;

@Data
@Slf4j
public class BaseApiService {

    @Resource
    RedisUtil redisUtil;

    /**
     * 10位数
     */
    private Integer decadeNum = 10;

    /**
     * 百位数
     */
    private Integer hundredsNum = 100;


    /**
     * dto 转换do
     *
     * @param dtoEntity dto
     * @param doClass 目标class
     */
    public static <Do> Do dtoToDo(Object dtoEntity, Class<Do> doClass) {
        return CommonBeanUtils.dtoToDo(dtoEntity, doClass);
    }

    /**
     * do转换成dto
     * @param doEntity do
     * @param dtoClass 目标class
     */
    public static <Dto> Dto doToDto(Object doEntity, Class<Dto> dtoClass) {
        return CommonBeanUtils.doToDto(doEntity, dtoClass);
    }

    //获取两个列表中的不同元素
    public <T> List<T>  getDiffInList1(List<T> list1, List<T> list2, BiPredicate<T,T> equals){
        List<T> diff = new ArrayList<>();
        for (T t : list1) {
            boolean flag = false;
            for (T t1 : list2) {
                if(equals.test(t,t1)) {
                    flag = true;
                    break;
                }
            }
            if(!flag){
                diff.add(t);
            }
        }
        return diff;
    }

    /**
     * code 三位数 不足十位数、百位补0
     * @param code 编码
     */
    public String getThreeDigitCode(Long code , String preFix){
        if (code < decadeNum){
            return preFix + "00" + code;
        } else if (code < hundredsNum ){
            return preFix + "0" + code;
        } else {
            return preFix + code.toString();
        }
    }

    /**
     * code 两位数 不足十位数、百位补0
     * @param code 编码
     */
    public String getTwoDigitCode(Long code , String preFix){
        if (code < decadeNum){
            return preFix + "0" + code;
        } else {
            return preFix + code.toString();
        }
    }


    /**
     * 2个JSONObject合并
     * @param source json源
     * @param target json目标
     */
    public static JSONObject deepMerge(JSONObject source, JSONObject target) throws JSONException {
        for (String key: source.keySet()) {
            Object value = source.get(key);
            if (!target.containsKey(key)) {
                // new value for "key":
                target.put(key, value);
            } else {
                // existing value for "key" - recursively deep merge:
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject)value;
                    deepMerge(valueJson, target.getJSONObject(key));
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }


    /**
     * 根据属性名获取属性值
     * */
    public Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
