package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfo;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.Loop;
import com.zhikuntech.intellimonitor.fanscada.domain.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

/**
 * @author 滕楠
 * @className FanIndexServiceImpl
 * @create 2021/6/15 11:22
 **/
@Service
public class FanIndexServiceImpl implements FanIndexService {

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void getFanBaseInfoList(String username) throws Exception {

        //GoldenUtil goldenUtil = new GoldenUtil();
        //通过mapper获取庚顿数据的对应关系

        if (webSocketServer.getClients().containsKey(username)) {
            //int[] ids = goldenUtil.getIds("fan");
            int[] ids = {21, 22, 23, 24};
            List<FanBaseInfo> list = new ArrayList<>(10);
            for (int i = 1; i <= 10; i++) {
                FanBaseInfo fanBaseInfo = new FanBaseInfo();
                fanBaseInfo.setFanNumber(i + "");
                list.add(fanBaseInfo);
            }
            goldenUtil.subscribeSnapshots(username, ids, (data) -> {
                if (!webSocketServer.getClients().containsKey(username)) {
                    return;
                } else {
                    List<FanBaseInfo> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);

                    if (null != dtos) {
                        List<Loop> loopList = new ArrayList<>();
                        for (FanBaseInfo dto : dtos) {
                            Loop loop = new Loop();
                            loop.setLoopNumber("");
                            loop.setFanBaseInfos(dtos);
                            loopList.add(loop);
                        }
                        String jsonString = JSONObject.toJSONString(loopList);
                        webSocketServer.sendMessage(jsonString, username);
                    }
                }
            });
        }

    }
}