package com.zhikuntech.intellimonitor.fanscada.domain.service;


import java.util.List;

public interface FanIndexService {


    /**
     * 查询回路
     * @return
     */
    void getFanBaseInfoList(String userName, List<Integer> list) throws Exception;
    void getFanBaseInfoList(String userName) throws Exception;

}
