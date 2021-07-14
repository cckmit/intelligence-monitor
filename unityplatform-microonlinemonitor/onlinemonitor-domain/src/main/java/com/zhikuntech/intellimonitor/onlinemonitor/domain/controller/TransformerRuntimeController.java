package com.zhikuntech.intellimonitor.onlinemonitor.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.dto.TransformerRuntimeDTO;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.TransformerService;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.impl.TransformerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 代志豪
 * 2021/7/12 14:22
 */
@RequestMapping("/onlineMonitor")
@RestController
public class TransformerRuntimeController {

    @Autowired
    private TransformerService transformerService;

    @PostMapping("/get")
    public BaseResponse get() {
//        CopyOnWriteArrayList<TransformerRuntimeDTO> queue = TransformerServiceImpl.list_land;
        Map<String,Object> map = new HashMap<>(2);
//        map.put("size",queue.size());
//        map.put("item",queue);
        return BaseResponse.success(map);
    }

}
