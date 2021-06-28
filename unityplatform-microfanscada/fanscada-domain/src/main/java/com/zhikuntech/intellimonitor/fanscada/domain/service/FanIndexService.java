package com.zhikuntech.intellimonitor.fanscada.domain.service;


import com.zhikuntech.intellimonitor.fanscada.domain.vo.LoopVO;

import java.util.List;

public interface FanIndexService {

    void getFanBaseInfoList(String userName) ;

    List<LoopVO> getFanBaseInfoList() ;

}
