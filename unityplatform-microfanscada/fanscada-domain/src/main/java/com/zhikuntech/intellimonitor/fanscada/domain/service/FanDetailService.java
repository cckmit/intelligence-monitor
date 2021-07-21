package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;

import java.util.List;
import java.util.Map;

/**
 * @author： DAI
 * @date： Created in 2021/7/21 13:59
 */
public interface FanDetailService {

    /**
     * 数据匹配
     *
     * @param backendList  backendList
     * @param number number
     * @param <T> t
     */
    <T> T getMatchData(T t, List<Integer> backendList, String number);

}
